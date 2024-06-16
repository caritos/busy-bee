package com.caritos.routes

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        staticResources("/static", "static")

        get("/") {
            call.respondRedirect("dashboard")
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