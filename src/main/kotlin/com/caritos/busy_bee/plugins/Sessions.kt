package com.caritos.busy_bee.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.sessions.*

data class UserSession(val username: String): Principal

fun Application.configureSession() {
    install(Sessions) {
        cookie<UserSession>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60
        }
    }
}