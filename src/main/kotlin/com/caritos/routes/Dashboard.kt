package com.caritos.routes

import com.caritos.dao.daoCourt
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

fun Route.dashboard() {
    route("dashboard") {
        get {
            call.respond(FreeMarkerContent("dashboard/index.ftl", model = null))
        }
    }
}