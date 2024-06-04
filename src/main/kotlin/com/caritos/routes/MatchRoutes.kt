package com.caritos.routes

import com.caritos.dao.daoMatch
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
        get {
            call.respond(FreeMarkerContent("matches/index.ftl", mapOf("matches" to daoMatch.getAll())))
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
           logger.info("match created:" + match?.id)
            call.respondRedirect("/matches/${match?.id}")
        }

        get("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("/matches/show.ftl", mapOf("match" to daoMatch.get(id))))
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