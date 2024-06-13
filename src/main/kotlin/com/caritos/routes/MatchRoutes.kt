package com.caritos.routes

import com.caritos.dao.daoCourt
import com.caritos.dao.daoMatch
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
import java.time.format.DateTimeFormatter
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.selectAll
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalDateTime

fun Route.matchRoutes() {
    val logger = LoggerFactory.getLogger("Routes")
    route("matches") {
//        get {
//            call.respond(FreeMarkerContent("matches/index.ftl", mapOf("matches" to daoMatch.getAll())))
//        }
//        get {
//    val matches = daoMatch.getAll()
//    val matchesWithSets = matches.map { match ->
//        match to (daoTennisSet.getAllForMatch(match.id) ?: emptyList())
//    }.toMap()
//    call.respond(FreeMarkerContent("matches/index.ftl", mapOf("matches" to matches, "matchesWithSets" to matchesWithSets)))
//}
        get {
            val players = daoPlayer.getAll().map { it.id.toString() to it.name }.toMap() // Assuming you have a method to get all players
            val courts = daoCourt.getAllCourts().map { it.id.toString() to it.name }.toMap()
    val matches = daoMatch.getAll()
    val matchesWithSets = matches.map { match ->
        match.id to (daoTennisSet.getAllForMatch(match.id) ?: emptyList())
    }.toMap()
    call.respond(FreeMarkerContent("matches/index.ftl", mapOf("matches" to matches, "matchesWithSets" to matchesWithSets, "players" to players, "courts" to courts)))
}

        get("new") {
            val players = transaction {
                Players.selectAll().map {
                    Player(it[Players.id].value, it[Players.name])
                }
            }
            val courts = transaction {
                Courts.selectAll().map {
                    Court(it[Courts.id].value, it[Courts.name], it[Courts.location])
                }
            }

            val dataModel = mapOf("players" to players,
                "courts" to courts)
            call.respond(FreeMarkerContent("matches/new.ftl", dataModel))
        }

        post {
            logger.info("inside post")
            val formParameters = call.receiveParameters()
            logger.info("formParameters: " + formParameters)
            // Convert date string to LocalDateTime
            val dateString = formParameters.getOrFail("date")
            logger.info("dateString: " + dateString)
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            val dateTime = LocalDateTime.parse(dateString, formatter)
            logger.info("dateTime: " + dateTime)
            val courtId = formParameters.getOrFail("court")
            logger.info("courtId:" + courtId)
            val winnerId = formParameters.getOrFail("player1")
            logger.info("winnerId:" + winnerId)
            val loserId = formParameters.getOrFail("player2")
            logger.info("loserId: " + loserId)
            val isDoubles = formParameters.getOrFail("isDoubles").equals("singles")
            val isDoublesBoolean = if(isDoubles.equals("doubles")) true else false
            logger.info("isDoubles: " + isDoubles)
            logger.info("will be adding match to database")
            val match= daoMatch.add(dateTime, courtId.toInt(), winnerId.toInt(), loserId.toInt(), isDoublesBoolean)
            logger.info("will be adding the set scores")
            if(match != null) {
                var setNumber = 1
                while (true) {
                    logger.info("setNumber value: " + setNumber)
                    val player1ScoreParam = formParameters["set${setNumber}_player1"]
                    val player2ScoreParam = formParameters["set${setNumber}_player2"]
                    logger.info("player1ScoreParam: " + player1ScoreParam)
                    logger.info("player2ScoreParam: " + player2ScoreParam)

                    if (player1ScoreParam == null || player2ScoreParam == null) {
                        break
                    }

                    val player1Score = player1ScoreParam.toInt()
                    val player2Score = player2ScoreParam.toInt()

                    logger.info("adding score for match ${match.id}, set $setNumber: $player1Score - $player2Score")
                    daoTennisSet.add(match.id, setNumber, player1Score, player2Score)

                    setNumber++
                }
            }

            logger.info("match created:" + match?.id)
            call.respondRedirect("/matches/${match?.id}")
        }

        get("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val match = daoMatch.get(id)
            val tennisSets = daoTennisSet.getAllForMatch(id) // Assuming you have a method to get all TennisSet for a match
            call.respond(FreeMarkerContent("/matches/show.ftl", mapOf("match" to match, "tennisSets" to tennisSets)))
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
                    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    val dateTime = LocalDateTime.parse(dateString, formatter)
                    val courtId = formParameters.getOrFail("courtId")
                    val winnerId = formParameters.getOrFail("winnerId")
                    val loserId = formParameters.getOrFail("loserId")
                    val isDoubles = formParameters.getOrFail("isDoubles")
                    daoMatch.edit(id, dateTime, courtId.toInt(), winnerId.toInt(), loserId.toInt(), isDoubles.toBoolean())
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