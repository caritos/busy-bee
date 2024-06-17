package com.caritos.routes

import com.caritos.dao.daoCourt
import com.caritos.dao.daoPlayer
import com.caritos.dao.daoTeam
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.slf4j.LoggerFactory

fun Route.dashboard() {
    val logger = LoggerFactory.getLogger("dashboard")
    route("dashboard") {
        get {
            val singlePlayerTeamsWithScores = daoTeam.getAllSinglePlayerTeamsWithScores()
            val sortedTeamsSingle = singlePlayerTeamsWithScores.sortedByDescending { it.second }
            val singlePlayerTeamsWithNames = sortedTeamsSingle.map { Pair(it.first.playerIds.map { playerId -> daoPlayer.get(playerId)?.name ?: "Unknown" }, it.second) }
            logger.info("singlePlayerTeamsWithNames: $singlePlayerTeamsWithNames")

            val doublePlayerTeamsWithScores = daoTeam.getAllDoublePlayerTeamsWithScores()
            val sortedTeamsDoubles = doublePlayerTeamsWithScores.sortedByDescending { it.second }
            val doublePlayerTeamsWithNames = sortedTeamsDoubles.map { Pair(it.first.playerIds.map { playerId -> daoPlayer.get(playerId)?.name ?: "Unknown" }, it.second) }
            logger.info("doublePlayerTeamsWithNames: $doublePlayerTeamsWithNames")

            call.respond(FreeMarkerContent("dashboard/index.ftl", mapOf("singlePlayerTeamsWithNames" to singlePlayerTeamsWithNames, "doublePlayerTeamsWithNames" to doublePlayerTeamsWithNames)))
        }
    }
}