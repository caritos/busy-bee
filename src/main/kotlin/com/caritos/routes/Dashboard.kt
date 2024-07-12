package com.caritos.routes

import com.caritos.dao.*
import com.caritos.models.MatchWithPlayerNames
import com.caritos.models.courtRepository
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
            val recentMatches = daoMatch.getRecentMatches(10).map { match ->
                val teamAName = daoTeam.getTeamName(match.teamAId)
                val teamAPlayerCount = daoTeam.getTeamPlayerCount(match.teamAId)
                val teamAScore = daoTeam.getTeamScore(match.teamAId)
                val teamBName = daoTeam.getTeamName(match.teamBId)
                val teamBPlayerCount = daoTeam.getTeamPlayerCount(match.teamBId)
                val teamBScore = daoTeam.getTeamScore(match.teamBId)
                logger.info("teamAName: $teamAName")
                logger.info("teamBName: $teamBName")
                logger.info("teamAPlayerCount: $teamAPlayerCount")
                logger.info("teamBPlayerCount: $teamBPlayerCount")
                logger.info("teamAScore: $teamAScore")
                logger.info("teamBScore: $teamBScore")

                MatchWithPlayerNames(
                    id = match.id,
                    date = match.date,
                    courtId = match.courtId,
                    courtName = courtRepository.courtById(match.courtId)?.name ?: "Unknown",
                    teamAId = match.teamAId.toString(),
                    teamANames = teamAName ?: "Unknown",
                    teamBId = match.teamBId.toString(),
                    teamBNames = teamBName ?: "Unknown",
                    score = daoTennisSet.getTennisSetsForMatch(match.id)
                )
            }

            val teamsWithNameAndScore = daoTeam.getTeamsWithNameAndScore()
            val singlesTeamsWithNameAndScore = teamsWithNameAndScore
                .filter { it.numberOfPlayers == 1 }
                .sortedByDescending { it.score }
        
            val doublesTeamsWithNameAndScore = teamsWithNameAndScore
                .filter { it.numberOfPlayers == 2 }
                .sortedByDescending { it.score }

            call.respond(FreeMarkerContent("dashboard/index.ftl", mapOf(
                "recentMatches" to recentMatches, 
                "singlesTeamsWithNameAndScore" to singlesTeamsWithNameAndScore,
                "doublesTeamsWithNameAndScore" to doublesTeamsWithNameAndScore
            )))
        }
    }
}
