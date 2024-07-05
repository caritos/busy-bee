package com.caritos.routes

import com.caritos.dao.*
import com.caritos.models.MatchWithPlayerNames
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
            val teamsWithScores = daoTennisSet.getTeamsWithScores()
            logger.info("teamsWithScores: $teamsWithScores")


            val recentMatches = daoMatch.getRecentMatches(10).map { match ->
                val teamAName = daoTeam.getTeamName(match.teamAId)
                val teamBName = daoTeam.getTeamName(match.teamBId)
                logger.info("teamAName: $teamAName")
                logger.info("teamBName: $teamBName")

                MatchWithPlayerNames(
                    id = match.id,
                    date = match.date,
                    courtId = match.courtId,
                    courtName = daoCourt.court(match.courtId)?.name ?: "Unknown",
                    teamAId = match.teamAId.toString(),
                    teamANames = teamAName ?: "Unknown",
                    teamBId = match.teamBId.toString(),
                    teamBNames = teamBName ?: "Unknown",
                    score = daoTennisSet.getTennisSetsForMatch(match.id)
                )
            }

            val singlePlayerTeamsWithScores = daoTeam.getAllSinglePlayerTeamsWithScores()
            val sortedTeamsSingle = singlePlayerTeamsWithScores.sortedByDescending { it.second }
            val singlePlayerTeamsWithNames = sortedTeamsSingle.map { Pair(it.first.playerIds.map { playerId -> daoPlayer.get(playerId)?.name ?: "Unknown" }, it.second) }
            logger.info("singlePlayerTeamsWithNames: $singlePlayerTeamsWithNames")

            val doublePlayerTeamsWithScores = daoTeam.getAllDoublePlayerTeamsWithScores()
            val sortedTeamsDoubles = doublePlayerTeamsWithScores.sortedByDescending { it.second }
            val doublePlayerTeamsWithNames = sortedTeamsDoubles.map { Pair(it.first.playerIds.map { playerId -> daoPlayer.get(playerId)?.name ?: "Unknown" }, it.second) }
            logger.info("doublePlayerTeamsWithNames: $doublePlayerTeamsWithNames")

            call.respond(FreeMarkerContent("dashboard/index.ftl", mapOf("recentMatches" to recentMatches, "singlePlayerTeamsWithNames" to singlePlayerTeamsWithNames, "doublePlayerTeamsWithNames" to doublePlayerTeamsWithNames)))
        }
    }
}

