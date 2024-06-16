package com.caritos.routes

import com.caritos.dao.daoTeam
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.team() {
    route("teams") {
        get {
            call.respond(FreeMarkerContent("teams/index.ftl", mapOf("teams" to daoTeam.getAll())))
        }

        get("new") {
            call.respond(FreeMarkerContent("teams/new.ftl", model = null))
        }

        post {
            val formParameters = call.receiveParameters()
            val playerId = formParameters.getOrFail("playerId")
            val team = daoTeam.add(setOf(playerId.toInt()))
            call.respondRedirect("/teams/${team?.id}")
        }

        get("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("/teams/show.ftl", mapOf("team" to daoTeam.get(id))))
        }

        get("{id}/edit") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("/teams/edit.ftl", mapOf("team" to daoTeam.get(id))))
        }

        post("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val formParameters = call.receiveParameters()
            when (formParameters.getOrFail("_action")) {
                "update" -> {
                    val playerId = formParameters.getOrFail("playerId")
                    daoTeam.edit(id, setOf(playerId.toInt()))
                    call.respondRedirect("/teams/$id")
                }

                "delete" -> {
                    daoTeam.delete(id)
                    call.respondRedirect("/teams")
                }
            }
        }
    }
}