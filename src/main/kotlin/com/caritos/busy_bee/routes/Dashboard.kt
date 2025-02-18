package com.caritos.busy_bee.routes

import com.caritos.busy_bee.db.courtRepository
import com.caritos.busy_bee.db.daoTennisSet
import com.caritos.busy_bee.db.matchRepository
import com.caritos.busy_bee.db.teamRepository
import com.caritos.busy_bee.models.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

fun Route.dashboard() {
    val logger = LoggerFactory.getLogger("dashboard")
    route("dashboard") {
        get {
            val recentMatches = matchRepository.getRecentMatches(10).map { match ->
                val teamAName = teamRepository.getTeamName(match.teamAId)
                val teamAPlayerCount = teamRepository.getTeamPlayerCount(match.teamAId)
                val teamAScore = teamRepository.getTeamScore(match.teamAId)
                val teamBName = teamRepository.getTeamName(match.teamBId)
                val teamBPlayerCount = teamRepository.getTeamPlayerCount(match.teamBId)
                val teamBScore = teamRepository.getTeamScore(match.teamBId)
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

            val teamsWithNameAndScore = teamRepository.getTeamsWithNameAndScore()
            val singlesTeamsWithNameAndScore = teamsWithNameAndScore
                .filter { it.numberOfPlayers == 1 }
                .sortedByDescending { it.score }
        
            val doublesTeamsWithNameAndScore = teamsWithNameAndScore
                .filter { it.numberOfPlayers == 2 }
                .sortedByDescending { it.score }

            val teamsWithWinningPercentages = teamRepository.getAllTeamsWithWinningPercentages()
            val singlesTeamsWithNameAndWinningPercentages = teamsWithWinningPercentages
                .filter { it.numberOfPlayers == 1 }
                .sortedByDescending { it.rate}
            val doublesTeamsWithNameAndWinningPercentages = teamsWithWinningPercentages
                .filter { it.numberOfPlayers == 2 }
                .sortedByDescending { it.rate}
            call.respond(FreeMarkerContent("dashboard/index.ftl", mapOf(
                "recentMatches" to recentMatches, 
                "singlesTeamsWithNameAndWinningPercentages" to singlesTeamsWithNameAndWinningPercentages,
                "doublesTeamsWithNameAndWinningPercentages" to doublesTeamsWithNameAndWinningPercentages,
                "singlesTeamsWithNameAndScore" to singlesTeamsWithNameAndScore,
                "doublesTeamsWithNameAndScore" to doublesTeamsWithNameAndScore
            )))
        }
    }
}
