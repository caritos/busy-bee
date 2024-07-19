package com.caritos.busy_bee.routes

import com.caritos.busy_bee.models.courtRepository
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.courtRoutes() {
    route("courts") {
        get {
            val courts = courtRepository.allCourts().sortedByDescending { it.name }
            call.respond(FreeMarkerContent("courts/index.ftl", mapOf("courts" to courts)))
        }

        get("new") {
            call.respond(FreeMarkerContent("courts/new.ftl", model = null))
        }

        post {
            val formParameters = call.receiveParameters()
            val name = formParameters.getOrFail("name")
            val location = formParameters.getOrFail("location")
            val court = courtRepository.addCourt(name, location)
            call.respondRedirect("/courts/${court?.id}")
        }

        get("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("/courts/show.ftl", mapOf("court" to courtRepository.courtById(id))))
        }

        get("{id}/edit") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("/courts/edit.ftl", mapOf("court" to courtRepository.courtById(id))))
        }

        post("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val formParameters = call.receiveParameters()
            when (formParameters.getOrFail("_action")) {
                "update" -> {
                    val name = formParameters.getOrFail("name")
                    val location = formParameters.getOrFail("location")
                    courtRepository.updateCourt(id, name, location)
                    call.respondRedirect("/courts/$id")
                }

                "delete" -> {
                    courtRepository.removeCourt(id)
                    call.respondRedirect("/courts")
                }
            }
        }

    }
}