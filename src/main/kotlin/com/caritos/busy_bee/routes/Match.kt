package com.caritos.busy_bee.routes

import com.caritos.busy_bee.db.*
import com.caritos.busy_bee.models.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Route.match() {
    val logger = LoggerFactory.getLogger("Routes")
    route("matches") {
        get {
            val matches = matchRepository.getAll().map { match ->
                val teamANames = teamRepository.getTeamName(match.teamAId)
                val teamBNames = teamRepository.getTeamName(match.teamBId)
                MatchWithPlayerNames(
                    id = match.id,
                    date = match.date,
                    courtId = match.courtId,
                    courtName = courtRepository.courtById(match.courtId)?.name ?: "Unknown",
                    teamAId = match.teamAId.toString(),
                    teamANames = teamANames,
                    teamBId = match.teamBId.toString(),
                    teamBNames = teamBNames,
                    score = daoTennisSet.getTennisSetsForMatch(match.id)
                )
            }.sortedByDescending { it.date }

            call.respond(FreeMarkerContent("matches/index.ftl", mapOf("matches" to matches)))
        }

        get("new") {
            val players = transaction {
                PlayerTable.selectAll().map {
                    Player(it[PlayerTable.id].value, it[PlayerTable.name])
                }.sortedBy { it.name }
            }
            val playersJson = Json.encodeToString(players)
            println("begin route matches/new")
            println(playersJson)
            println("end route matches/new")
            val courts = courtRepository.allCourts().sortedBy { it.name }
            val dataModel = mapOf("playersJson" to playersJson, "players" to players,
                "courts" to courts)
            call.respond(FreeMarkerContent("matches/new.ftl", dataModel))
        }

        post {
            logger.info("inside post")
            val formParameters = call.receiveParameters()
            val formParametersMap = formParameters.entries().associate { it.key to it.value.toList() }
            logger.info("formParameters: $formParameters")
            // Convert date string to LocalDateTime
            val dateString = formParameters.getOrFail("date")
            logger.info("dateString: $dateString")
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE
            val date = LocalDate.parse(dateString, formatter)
            logger.info("dateTime: $date")
            val courtId = formParameters.getOrFail("court")
            logger.info("courtId:$courtId")
            val teamAPlayerIds = formParametersMap.filterKeys { it.startsWith("teamAContainerPlayer") }.values.flatten()
            val teamBPlayerIds = formParametersMap.filterKeys { it.startsWith("teamBContainerPlayer") }.values.flatten()
            logger.info(teamAPlayerIds.toString())
            logger.info(teamBPlayerIds.toString())
            // I need to check if this set of players already exists in the teams table
            // if it doesn't exist, create it
            val teamAId = teamRepository.createTeam("", teamAPlayerIds.map { it.toInt() }.toSet())
            val teamBId = teamRepository.createTeam("", teamBPlayerIds.map { it.toInt() }.toSet())

            logger.info("will be adding match to database")
            val match= matchRepository.add(date, courtId.toInt(), teamAId, teamBId)
            logger.info("will be adding the set scores")
            if(match != null) {
                var setNumber = 1
                while (true) {
                    logger.info("setNumber value: $setNumber")
                    val teamAScoreParam = formParameters["set${setNumber}_teamA"]
                    val teamBScoreParam = formParameters["set${setNumber}_teamB"]
                    logger.info("teamA: $teamAScoreParam")
                    logger.info("teamB: $teamBScoreParam")

                    if (teamAScoreParam == null || teamBScoreParam == null) {
                        break
                    }

                    val teamAScore = teamAScoreParam.toInt()
                    val teamBScore = teamBScoreParam.toInt()

                    logger.info("adding score for match ${match.id}, set $setNumber: $teamAScore - $teamBScore")
                    daoTennisSet.add(match.id, setNumber, teamAId, teamBId, teamAScore, teamBScore)

                    setNumber++
                }
            }

            logger.info("match created:" + match?.id)
            call.respondRedirect("/matches")
        }

        get("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val match = matchRepository.get(id)
            assert(match != null)
            if(match != null) {
                val teamANames = teamRepository.getTeamName(match.teamAId)
                val teamBNames = teamRepository.getTeamName(match.teamBId)
                val matchWithPlayerName = MatchWithPlayerNames(
                    id = match.id,
                    date = match.date,
                    courtId = match.courtId,
                    courtName = courtRepository.courtById(match.courtId)?.name ?: "Unknown",
                    teamAId = match.teamAId.toString(),
                    teamANames = teamANames,
                    teamBId = match.teamBId.toString(),
                    teamBNames = teamBNames,
                    score = daoTennisSet.getTennisSetsForMatch(match.id)
                )
                call.respond(FreeMarkerContent("/matches/show.ftl", mapOf("match" to matchWithPlayerName)))
            }
        }

        get("{id}/edit") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("/matches/edit.ftl", mapOf("match" to matchRepository.get(id))))
        }

        post("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val formParameters = call.receiveParameters()
            when (formParameters.getOrFail("_action")) {
                "update" -> {
                    val dateString = formParameters.getOrFail("date")
                    val formatter = DateTimeFormatter.ISO_LOCAL_DATE
                    val dateTime = LocalDate.parse(dateString, formatter)
                    val courtId = formParameters.getOrFail("courtId")
                    val teamAId = formParameters.getOrFail("teamAId")
                    val teamBId = formParameters.getOrFail("teamBId")
                    matchRepository.edit(id, dateTime, courtId.toInt(), teamAId.toInt(), teamBId.toInt())
                    call.respondRedirect("/matches/$id")
                }

                "delete" -> {
                    matchRepository.delete(id)
                    call.respondRedirect("/matches")
                }
            }
        }
    }
}