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

data class UserSession(val name: String)

fun Application.configureRouting() {
    val log = LoggerFactory.getLogger("Application")
    routing {
        // Handle the login form submission
        authenticate("auth-form") {
            post("/login") {
                log.info("inside authenticate auth form")
                val principal = call.principal<UserIdPrincipal>()
                if (principal != null) {
                    call.sessions.set(UserSession(principal.name))
                    call.respondText("Logged in as ${principal.name}")
                }
            }
        }

        // Protected route
        authenticate("auth-form") {
            get("/protected") {
                val principal = call.principal<UserIdPrincipal>()
                call.respondText("Hello, ${principal!!.name}")
            }
        }

        get("/logout") {
            call.sessions.clear<UserSession>()
            call.respondText("Logged out")
        }

        get("/protected") {
            val session = call.sessions.get<UserSession>()
            if (session == null) {
                call.respond(HttpStatusCode.Unauthorized, "Unauthorized")
            } else {
                call.respondText("Hello, ${session.name}")
            }
        }
        get("/login") {
            log.info("Serving login page")
            call.respond(FreeMarkerContent("login.ftl", model = null))
        }


        get("/hello") {
            log.debug("inside get /hello")
            call.respond(FreeMarkerContent("hello.ftl", model = null))
        }

        get("/") {
            call.respondRedirect("articles")
        }

        staticResources("/static", "static")

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