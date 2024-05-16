package com.caritos.plugins

import io.ktor.http.*
import kotlinx.html.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            val name = "Ktor"
            call.respondHtml(HttpStatusCode.OK) {
                head {
                    title {
                        +name
                    }
                }
                body {
                    h1 {
                        +"Hello from $name!"
                    }
                }
            }
        }
    }
}