package com.caritos.routes

import com.caritos.dao.dao
import com.caritos.dao.daoPlayer
import com.caritos.dao.daoCourt
import com.caritos.models.Users
import com.caritos.plugins.UserSession
import com.caritos.plugins.generateSalt
import com.caritos.plugins.hashPassword
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.util.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

fun Application.configureRouting() {
    val log = LoggerFactory.getLogger("Application")
    routing {
        staticResources("/static", "static")

        get("/login") {
            log.info("Serving login page")
            call.respond(FreeMarkerContent("auth/login.ftl", model = null))
        }

        get("/signup") {
            log.info("serving signup page")
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
                Users.insert {
                    it[Users.username] = username
                    it[Users.password] = hashedPassword
                    it[Users.salt] = salt
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
            log.info("logging out")
            call.sessions.clear<UserSession>()
            call.respondRedirect("/login")
        }

        authenticate("auth-session") {
            get("/dashboard") {
                log.info("inside dashboard")
                call.respond(FreeMarkerContent("dashboard/index.ftl", model = null))
            }
        }

        get("/") {
            call.respondRedirect("articles")
        }

        get("/matches") {
            call.respond(dao.getAllMatches())
        }

        get("/teams") {
            call.respond(dao.getAllTeams())
        }

        get("/sets") {
            call.respond(dao.getAllSets())
        }

        get("/rankings") {
            val rankings = dao.getRankings()
            call.respond(rankings)
        }


        courtRoutes()
        articleRoutes()
        playerRoutes()
    }

}