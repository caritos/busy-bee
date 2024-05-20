package com.caritos

import com.caritos.dao.DatabaseSingleton
import com.caritos.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.auth.*


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseSingleton.init()
    configureTemplating()
    configureSessions()
    configureAuthentication()
    configureRouting()
}
