package com.caritos.routes

import com.caritos.db.UserTable
import com.caritos.plugins.UserSession
import com.caritos.plugins.generateSalt
import com.caritos.plugins.hashPassword
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

fun Route.auth() {
    val logger = LoggerFactory.getLogger("auth")

    get("/login") {
        logger.info("Serving login page")
        call.respond(FreeMarkerContent("auth/login.ftl", model = null))
    }

    get("/signup") {
        logger.info("serving signup page")
        call.respond(FreeMarkerContent("auth/signup.ftl", model = null))
    }

    post("/signup") {
        val params = call.receiveParameters()
        val username = params["username"]
        val password = params["password"]

        if (username.isNullOrBlank() || password.isNullOrBlank()) {
            call.respond(HttpStatusCode.BadRequest, "Missing username or password")
            return@post
        }

        val salt = generateSalt()
        val hashedPassword = hashPassword(password, salt)

        transaction {
            UserTable.insert {
                it[UserTable.username] = username
                it[UserTable.password] = hashedPassword
                it[UserTable.salt] = salt
            }
        }

        call.respond(HttpStatusCode.Created, "User registered successfully")
    }

    // Handle the login form submission
    authenticate("auth-form") {
        post("/login") {
            val principal = call.principal<UserIdPrincipal>()
            if (principal != null) {
                call.sessions.set(UserSession(principal.name))
                call.respondRedirect("/dashboard")
            } else {
                call.respondText("Authentication failed")
            }
        }
    }

    get("/logout") {
        logger.info("logging out")
        call.sessions.clear<UserSession>()
        call.respondRedirect("/login")
    }

    // Handle the login form submission
    authenticate("auth-session") {
    }
}