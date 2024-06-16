package com.caritos.routes

import com.caritos.dao.daoTennisSet
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

/**
 *     val matchId = integer("match_id").references(Matches.id)
 *     val setNumber = integer("set_number")
 *     val player1Score = integer("player1_score")
 *     val player2Score = integer("player2_score")
 */
fun Route.tennisSet() {
    route("tennissets") {
        get {
            call.respond(FreeMarkerContent("tennissets/index.ftl", mapOf("tennissets" to daoTennisSet.getAll())))
        }

        get("new") {
            call.respond(FreeMarkerContent("tennissets/new.ftl", model = null))
        }

        post {
            val formParameters = call.receiveParameters()
            val setNumber = formParameters.getOrFail("set_number")
            val matchId = formParameters.getOrFail("match_id")
            val player1Score = formParameters.getOrFail("player1_score")
            val player2Score = formParameters.getOrFail("player2_score")
            val tennisSet = daoTennisSet.add(matchId.toInt(), setNumber.toInt(), player1Score.toInt(), player2Score.toInt())
            call.respondRedirect("/tennissets/${tennisSet?.id}")
        }

        get("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("/tennissets/show.ftl", mapOf("tennisSet" to daoTennisSet.get(id))))
        }

        get("{id}/edit") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            call.respond(FreeMarkerContent("/tennissets/edit.ftl", mapOf("tennisSet" to daoTennisSet.get(id))))
        }

        post("{id}") {
            val id = call.parameters.getOrFail<Int>("id").toInt()
            val formParameters = call.receiveParameters()
            when (formParameters.getOrFail("_action")) {
                "update" -> {
                    val setNumber = formParameters.getOrFail("set_number")
                    val matchId = formParameters.getOrFail("match_id")
                    val player1Score = formParameters.getOrFail("player1_score")
                    val player2Score = formParameters.getOrFail("player2_score")
                    daoTennisSet.edit(id, matchId.toInt(), setNumber.toInt(), player1Score.toInt(), player2Score.toInt())
                    call.respondRedirect("/tennissets/$id")
                }

                "delete" -> {
                    daoTennisSet.delete(id)
                    call.respondRedirect("/tennissets")
                }
            }
        }
    }
}