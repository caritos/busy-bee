package com.caritos.routes

import com.caritos.dao.daoCourt
import com.caritos.dao.daoPlayer
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.courtRoutes() {
    route("courts") {
        get {
            val courts = daoCourt.getAllCourts().sortedByDescending { it.name }
            call.respond(FreeMarkerContent("courts/index.ftl", mapOf("courts" to courts)))
        }

        get("new") {
            call.respond(FreeMarkerContent("courts/new.ftl", model = null))
        }

        post {
            val formParameters = call.receiveParameters()
            val name = formParameters.getOrFail("name")
            val location = formParameters.getOrFail("location")
            val court = daoCourt.addCourt(name, location)
            call.respondRedirect("/courts/${court?.id}")
        }

        get("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("/courts/show.ftl", mapOf("court" to daoCourt.court(id))))
        }

        get("{id}/edit") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("/courts/edit.ftl", mapOf("court" to daoCourt.court(id))))
        }

        post("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val formParameters = call.receiveParameters()
            when (formParameters.getOrFail("_action")) {
                "update" -> {
                    val name = formParameters.getOrFail("name")
                    val location = formParameters.getOrFail("location")
                    daoCourt.editCourt(id, name, location)
                    call.respondRedirect("/courts/$id")
                }

                "delete" -> {
                    daoCourt.deleteCourt(id)
                    call.respondRedirect("/courts")
                }
            }
        }

    }
}