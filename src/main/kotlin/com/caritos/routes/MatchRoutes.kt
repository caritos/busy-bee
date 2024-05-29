package com.caritos.routes

import com.caritos.dao.daoMatch
import com.caritos.models.Matches
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Route.matchRoutes() {
    route("matches") {
        get {
            call.respond(FreeMarkerContent("matches/index.ftl", mapOf("matches" to daoMatch.getAll())))
        }

        get("new") {
            call.respond(FreeMarkerContent("matches/new.ftl", model = null))
        }

        post {
            val formParameters = call.receiveParameters()
            // Convert date string to LocalDateTime
            val dateString = formParameters.getOrFail("date")
            val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            val dateTime = LocalDateTime.parse(dateString, formatter)
            val courtId = formParameters.getOrFail("courtId")
            val winnerId = formParameters.getOrFail("winnerId")
            val loserId = formParameters.getOrFail("loserId")
            val isDoubles = formParameters.getOrFail("isDoubles")
            
            val match= daoMatch.add(dateTime, courtId.toInt(), winnerId.toInt(), loserId.toInt(), isDoubles.toBoolean())
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