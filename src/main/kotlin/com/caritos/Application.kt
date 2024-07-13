package com.caritos

import com.caritos.db.DatabaseSingleton
import com.caritos.plugins.*
import com.caritos.routes.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    DatabaseSingleton.init(environment.config)
    configureTemplating()
    configureSession()
    configureAuthentication()
    configureRouting()
}
