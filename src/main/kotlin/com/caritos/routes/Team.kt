package com.caritos.routes

import com.caritos.models.teamRepository
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.slf4j.LoggerFactory

fun Route.team() {
    route("teams") {
        val logger = LoggerFactory.getLogger("Teams")
        get {
            val teams = teamRepository.getAll()
            logger.info("teams" + teams.toString())
            call.respond(FreeMarkerContent("teams/index.ftl", mapOf("teams" to teams )))
        }

        get("new") {
            call.respond(FreeMarkerContent("teams/new.ftl", model = null))
        }

        post {
            val formParameters = call.receiveParameters()
            val playerId = formParameters.getOrFail("playerId")
            val team = teamRepository.add(setOf(playerId.toInt()))
            call.respondRedirect("/teams/${team?.id}")
        }

        get("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("/teams/show.ftl", mapOf("team" to teamRepository.get(id))))
        }

        get("{id}/edit") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("/teams/edit.ftl", mapOf("team" to teamRepository.get(id))))
        }

        post("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val formParameters = call.receiveParameters()
            when (formParameters.getOrFail("_action")) {
                "update" -> {
                    val name = formParameters.getOrFail("name")
                    teamRepository.edit(id, name)
                    call.respondRedirect("/teams/$id")
                }

                "delete" -> {
                    teamRepository.delete(id)
                    call.respondRedirect("/teams")
                }
            }
        }
    }
}