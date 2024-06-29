package com.caritos.routes

import com.caritos.dao.daoCourt
import com.caritos.dao.daoMatch
import com.caritos.dao.daoTeam
import com.caritos.dao.daoPlayer
import com.caritos.dao.daoTennisSet
import com.caritos.models.Court
import com.caritos.models.Courts
import com.caritos.models.Player
import com.caritos.models.Players
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.format.DateTimeFormatter
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime

fun Route.match() {
    val logger = LoggerFactory.getLogger("Routes")
    route("matches") {
        get {
            val teams = daoTeam.getAll().map { it.id.toString() to it.playerIds.map { playerId -> daoPlayer.get(playerId)?.name ?: "Unknown" }.joinToString(", ") }.toMap()
            logger.info("teams" + teams.toString())
            val players = daoPlayer.getAll().map { it.id.toString() to it.name }.toMap() // Assuming you have a method to get all players
            val playersJson = Json.encodeToString(players)
            val courts = daoCourt.getAllCourts().map { it.id.toString() to it.name }.toMap()
            val matches = daoMatch.getAll()
            val matchesWithSets = matches.map { match ->
                match.id to (daoTennisSet.getAllForMatch(match.id) ?: emptyList())
            }.toMap()
            call.respond(FreeMarkerContent("matches/index.ftl", mapOf("teams" to teams, "matches" to matches, "matchesWithSets" to matchesWithSets, "playersJson" to playersJson, "courts" to courts)))
        }

        get("new") {
            val players = transaction {
                Players.selectAll().map {
                    Player(it[Players.id].value, it[Players.name])
                }.sortedBy { it.name }
            }
            val playersJson = Json.encodeToString(players)
            println("begin route matches/new")
            println(playersJson)
            println("end route matches/new")
            val courts = transaction {
                Courts.selectAll().map {
                    Court(it[Courts.id].value, it[Courts.name], it[Courts.location])
                }
            }

            val dataModel = mapOf("playersJson" to playersJson, "players" to players,
                "courts" to courts)
            call.respond(FreeMarkerContent("matches/new.ftl", dataModel))
        }

        post {
            logger.info("inside post")
            val formParameters = call.receiveParameters()
            val formParametersMap = formParameters.entries().associate { it.key to it.value.toList() }
            logger.info("formParameters: " + formParameters)
            // Convert date string to LocalDateTime
            val dateString = formParameters.getOrFail("date")
            logger.info("dateString: " + dateString)
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE
            val date = LocalDate.parse(dateString, formatter)
            logger.info("dateTime: " + date)
            val courtId = formParameters.getOrFail("court")
            logger.info("courtId:" + courtId)
            val teamAPlayerIds = formParametersMap.filterKeys { it.startsWith("teamAContainerPlayer") }.values.flatten()
            val teamBPlayerIds = formParametersMap.filterKeys { it.startsWith("teamBContainerPlayer") }.values.flatten()
            logger.info(teamAPlayerIds.toString())
            logger.info(teamBPlayerIds.toString())
            // i need to check if this set of players already exists in the teams table
            // if it doesn't exist, create it
            val teamAId = daoTeam.getOrCreateTeam(teamAPlayerIds.map { it.toInt() }.toSet())
            val teamBId = daoTeam.getOrCreateTeam(teamBPlayerIds.map { it.toInt() }.toSet())

            logger.info("will be adding match to database")
            val match= daoMatch.add(date, courtId.toInt(), teamAId.toInt(), teamBId.toInt())
            logger.info("will be adding the set scores")
            if(match != null) {
                var setNumber = 1
                while (true) {
                    logger.info("setNumber value: " + setNumber)
                    val teamAScoreParam = formParameters["set${setNumber}_teamA"]
                    val teamBScoreParam = formParameters["set${setNumber}_teamB"]
                    logger.info("teamA: " + teamAScoreParam)
                    logger.info("teamB: " + teamBScoreParam)

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
            call.respondRedirect("/matches/${match?.id}")
        }

        get("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val match = daoMatch.get(id)
            assert(match != null)
            val teams = daoTeam.getAll().map { it.id.toString() to it.playerIds.map { playerId -> daoPlayer.get(playerId)?.name ?: "Unknown" }.joinToString(", ") }.toMap()
            logger.info("teams" + teams.toString())
            val courts = daoCourt.getAllCourts().map { it.id.toString() to it.name }.toMap()
            val tennisSets = daoTennisSet.getAllForMatch(id) // Assuming you have a method to get all TennisSet for a match
            call.respond(FreeMarkerContent("/matches/show.ftl", mapOf("courts" to courts, "match" to match, "tennisSets" to tennisSets, "teams" to teams)))
        }

        get("{id}/edit") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("/matches/edit.ftl", mapOf("match" to daoMatch.get(id))))
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
                    daoMatch.edit(id, dateTime, courtId.toInt(), teamAId.toInt(), teamBId.toInt())
                    call.respondRedirect("/matches/$id")
                }

                "delete" -> {
                    daoMatch.delete(id)
                    call.respondRedirect("/matches")
                }
            }
        }
    }
}