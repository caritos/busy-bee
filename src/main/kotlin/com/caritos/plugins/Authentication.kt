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
                    log.info("credentials pass")
                    UserIdPrincipal(credentials.name)
                } else {
                    log.info("credentials fail")
                    null
                }
            }
        }
        session<UserSession>("auth-session") {
            validate { session ->
                log.debug("validating session")
                if(session.name.startsWith("use")) {
                    log.info("session name is correct")
                    session
                } else {
                    log.info("session name incorrect")
                    null
                }
            }
            challenge {
                log.info("challenge at session<UserSession")
                call.respondRedirect("/login")
            }
        }
    }
}