package com.caritos.routes

import com.caritos.dao.daoPlayer
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.player() {
    route("players") {
        get {
            val players = daoPlayer.getAll().sortedByDescending { it.name }
            call.respond(FreeMarkerContent("players/index.ftl", mapOf("players" to players)))
        }

        get("new") {
            call.respond(FreeMarkerContent("players/new.ftl", model = null))
        }

        post {
            val formParameters = call.receiveParameters()
            val name = formParameters.getOrFail("name")
            val player = daoPlayer.add(name)
            call.respondRedirect("/players")
        }

        get("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("/players/show.ftl", mapOf("player" to daoPlayer.get(id))))
        }

        get("{id}/edit") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("/players/edit.ftl", mapOf("player" to daoPlayer.get(id))))
        }

        post("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val formParameters = call.receiveParameters()
            when (formParameters.getOrFail("_action")) {
                "update" -> {
                    val name = formParameters.getOrFail("name")
                    daoPlayer.edit(id, name)
                    call.respondRedirect("/players/$id")
                }

                "delete" -> {
                    daoPlayer.delete(id)
                    call.respondRedirect("/players")
                }
            }
        }
    }
}