package com.caritos

import com.caritos.plugins.*
import com.caritos.routes.configureRouting
import configureDatabase
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost", module = Application::module).start(wait = true)
}

fun Application.module() {
    configureDatabase()
    configureTemplating()
    configureSession()
    configureAuthentication()
    configureRouting()
}
