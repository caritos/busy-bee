package com.caritos.busy_bee.routes

import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        staticResources("/static", "static")

        get("/") {
            call.respondRedirect("dashboard")
        }

        get("/about") {
            call.respond(FreeMarkerContent("/about/index.ftl", null ))
        }

        auth()
        dashboard()
        courtRoutes()
        player()
        match()
        team()
        tennisSet()
    }

}