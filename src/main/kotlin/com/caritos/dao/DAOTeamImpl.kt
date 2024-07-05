package com.caritos.dao

import com.caritos.dao.DatabaseSingleton.dbQuery
import com.caritos.models.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class DAOTeamImpl : DAOTeam {
    val logger = LoggerFactory.getLogger("DAOTeamImpl")

    override suspend fun createTeam(name: String, playerIds: Set<Int>): Int {
        return transaction {
            // Fetch all existing teams and their players
            val existingTeams = TeamPlayers
                .slice(TeamPlayers.teamId, TeamPlayers.playerId)
                .selectAll()
                .groupBy { it[TeamPlayers.teamId] }
                .mapValues { entry -> entry.value.map { it[TeamPlayers.playerId] }.toSet() }

            // Check if the new set of players already exists
            val existingTeamId = existingTeams.entries.find { it.value == playerIds }?.key
            if (existingTeamId != null) {
                existingTeamId // Return the ID of the existing team
            } else {
                // Create the new team
                val teamId = Teams.insertAndGetId {
                    it[Teams.name] = name
                }

                // Insert players into TeamPlayers
                playerIds.forEach { playerId ->
                    TeamPlayers.insert {
                        it[TeamPlayers.playerId] = playerId
                        it[TeamPlayers.teamId] = teamId.value
                    }
                }
                teamId.value // Return the ID of the newly created team
            }
        }
    }

    private fun resultRowToTeam(row: ResultRow) = Team(
        id = row[Teams.id].value,
        name = row[Teams.name],
    )

    override suspend fun getAll(): List<Team> {
        return transaction {
            Teams.selectAll().map {
                Team(
                    id = it[Teams.id].value,
                    name = it[Teams.name],
                )
            }
        }
    }

    override suspend fun get(id: Int): Team? = dbQuery {
       Teams.select { Teams.id eq id }
            .map(::resultRowToTeam)
            .singleOrNull()
    }

    override suspend fun add(playerIds: Set<Int>): Team? = dbQuery {
        // Fetch player names based on playerIds
        val playerNames = Players.select { Players.id inList playerIds }
                            .map { it[Players.name] }
                            .joinToString(" ")
        val insertStatement = Teams.insert {
            it[Teams.name] = playerNames
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTeam)
    }

    override suspend fun edit(id: Int, name: String): Boolean = dbQuery {
        Teams.update({ Teams.id eq id }) {
            it[Teams.name] = name
        } > 0
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        Teams.deleteWhere { Teams.id eq id } > 0
    }

    override suspend fun getTeamsWithNameAndScore(): List<TeamWithNameAndScore> = dbQuery {
        Teams.selectAll().map { row ->
            val teamId = row[Teams.id].value
            val teamName = getTeamName(teamId)
            val playerCount = getTeamPlayerCount(teamId)
            val score = getTeamScore(teamId)
            TeamWithNameAndScore(teamId, teamName, playerCount, score)
        }
    }



    override suspend fun getAllTeamsWithScores(): List<Pair<Team, Int>> = dbQuery {
        Teams.selectAll().map { row ->
            val team = resultRowToTeam(row)
            val score = getTeamScore(team.id)
            team to score
        }
    }

    // get the team name from the player's name associated with the team
    // need iterate through the TeamPlayers table and get the player's name from the Players table
    override suspend fun getTeamName(teamId: Int): String = dbQuery {
        val playerNames = TeamPlayers
            .innerJoin(Players, { TeamPlayers.playerId }, { Players.id })
            .slice(Players.name)
            .select { TeamPlayers.teamId eq teamId }
            .map { it[Players.name] }
            .sorted() // Sort the names alphabetically
        
        playerNames.joinToString(" ")
    }

    override suspend fun getTeamPlayerCount(teamId: Int): Int = dbQuery {
        TeamPlayers.select { TeamPlayers.teamId eq teamId }.count().toInt()
    }

    override suspend fun getTeamScore(teamId: Int): Int = dbQuery {
        TennisSets.select { (TennisSets.teamAId eq teamId) or (TennisSets.teamBId eq teamId) }
            .count { 
                (it[TennisSets.teamAId] == teamId && it[TennisSets.teamAScore] > it[TennisSets.teamBScore]) ||
                (it[TennisSets.teamBId] == teamId && it[TennisSets.teamBScore] > it[TennisSets.teamAScore])
            }
    }





}


val daoTeam : DAOTeam = DAOTeamImpl().apply {
    runBlocking {
        if(getAll().isEmpty()) {
        }
    }
}