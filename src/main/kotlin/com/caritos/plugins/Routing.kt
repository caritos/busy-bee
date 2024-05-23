package com.caritos.plugins

import com.caritos.dao.dao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.freemarker.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.util.*
import kotlinx.html.*
import org.slf4j.LoggerFactory

fun Application.configureRouting() {
    val log = LoggerFactory.getLogger("Application")
    routing {
        staticResources("/static", "static")

        get("/login") {
            log.info("Serving login page")
            call.respond(FreeMarkerContent("login.ftl", model = null))
        }

        // Handle the login form submission
        authenticate("auth-form") {
            post("/login") {
                val username = call.principal<UserIdPrincipal>()?.name.toString()
                if (username != null) {
                    log.info("principal is not null")
                    call.sessions.set(UserSession(name = username, count = 1))
                    call.respondRedirect("/dashboard")
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
                val userSession = call.principal<UserSession>()
                if (userSession == null) {
                    log.info("user session is null")
                    call.respondRedirect("/login")
                } else {
                    log.info("session variable exist, go to dashboard.ftl")
                    call.sessions.set(userSession?.copy(count = userSession.count + 1))
                    call.respond(FreeMarkerContent("dashboard.ftl", model = null))
                }
            }
        }


        get("/") {
            call.respondRedirect("articles")
        }


        route("articles") {
            get {
                call.respond(FreeMarkerContent("index.ftl", mapOf("articles" to dao.allArticles())))
            }
            get("new") {
                call.respond(FreeMarkerContent("new.ftl", model = null))
            }
            post {
                val formParameters = call.receiveParameters()
                val title = formParameters.getOrFail("title")
                val body = formParameters.getOrFail("body")
                val article = dao.addNewArticle(title, body)
                call.respondRedirect("/articles/${article?.id}")
            }
            get("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                call.respond(FreeMarkerContent("show.ftl", mapOf("article" to dao.article(id))))
            }
            get("{id}/edit") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                call.respond(FreeMarkerContent("edit.ftl", mapOf("article" to dao.article(id))))
            }
            post("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                val formParameters = call.receiveParameters()
                when (formParameters.getOrFail("_action")) {
                    "update" -> {
                        val title = formParameters.getOrFail("title")
                        val body = formParameters.getOrFail("body")
                        dao.editArticle(id, title, body)
                        call.respondRedirect("/articles/$id")
                    }
                    "delete" -> {
                        dao.deleteArticle(id)
                        call.respondRedirect("/articles")
                    }
                }
            }
        }
    }
}