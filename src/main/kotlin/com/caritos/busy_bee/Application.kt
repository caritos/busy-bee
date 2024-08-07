package com.caritos.busy_bee

import com.caritos.busy_bee.plugins.DatabaseSingleton
import com.caritos.busy_bee.plugins.*
import com.caritos.busy_bee.routes.configureRouting
import io.ktor.server.application.*
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
