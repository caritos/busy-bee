package com.caritos.plugins

import io.ktor.server.application.*
import io.ktor.server.sessions.*

data class UserSession(val userId: String)

fun Application.configureSession() {
    install(Sessions) {
        cookie<UserSession>("USER_SESSION")
    }
}