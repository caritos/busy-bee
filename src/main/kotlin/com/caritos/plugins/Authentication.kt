package com.caritos.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory

fun Application.configureAuthentication() {
    val log = LoggerFactory.getLogger("Application")
    install(Authentication) {
        form("auth-form") {
            log.info("inside authentication")
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                log.info("credentials.name: ${credentials.name}")
                log.info("credentials.password: ${credentials.password}")
                if (credentials.name == "user" && credentials.password == "password") {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }

            challenge {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            }
        }
    }
}