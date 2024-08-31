package com.caritos.busy_bee.db

import com.caritos.busy_bee.plugins.DatabaseSingleton.dbQuery
import com.caritos.busy_bee.plugins.DatabaseSingleton.suspendTransaction
import com.caritos.busy_bee.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class SQLiteTeamRepository: TeamRepository {

    override suspend fun createTeam(name: String, playerIds: Set<Int>): Int {
        return transaction {
            // Fetch all existing teams and their players
            val existingTeams = TeamPlayersTable
                .slice(TeamPlayersTable.teamId, TeamPlayersTable.playerId)
                .selectAll()
                .groupBy { it[TeamPlayersTable.teamId] }
                .mapValues { entry -> entry.value.map { it[TeamPlayersTable.playerId] }.toSet() }

            // Check if the new set of players already exists
            val existingTeamId = existingTeams.entries.find { it.value == playerIds }?.key
            if (existingTeamId != null) {
                existingTeamId // Return the ID of the existing team
            } else {
                // Create the new team
                val teamId = TeamTable.insertAndGetId {
                    it[TeamTable.name] = name
                }

                // Insert players into TeamPlayers
                playerIds.forEach { playerId ->
                    TeamPlayersTable.insert {
                        it[TeamPlayersTable.playerId] = playerId
                        it[TeamPlayersTable.teamId] = teamId.value
                    }
                }
                teamId.value // Return the ID of the newly created team
            }
        }
    }

    private fun resultRowToTeam(row: ResultRow) = Team(
        id = row[TeamTable.id].value,
        name = row[TeamTable.name],
    )

    override suspend fun allTeams(): List<Team> = suspendTransaction {
        TeamDAO.all().map(::daoToModel)
    }

    override suspend fun teamById(id: Int): Team? = suspendTransaction{
        TeamDAO
            .find { ( TeamTable.id eq id) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun addTeam(playerIds: Set<Int>): Team? = dbQuery {
        // Fetch player names based on playerIds
        val playerNames = PlayerTable.select { PlayerTable.id inList playerIds }
            .map { it[PlayerTable.name] }
            .joinToString(" ")
        val insertStatement = TeamTable.insert {
            it[name] = playerNames
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTeam)
    }

    override suspend fun updateTeam(id: Int, name: String): Boolean = dbQuery {
        TeamTable.update({ TeamTable.id eq id }) {
            it[TeamTable.name] = name
        } > 0
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        TeamTable.deleteWhere { TeamTable.id eq id } > 0
    }

    override suspend fun getTeamsWithNameAndScore(): List<TeamWithNameAndScore> = dbQuery {
        TeamTable.selectAll().map { row ->
            val teamId = row[TeamTable.id].value
            val teamName = getTeamName(teamId)
            val playerCount = getTeamPlayerCount(teamId)
            val score = getTeamScore(teamId)
            TeamWithNameAndScore(teamId, teamName, playerCount, score)
        }
    }

    override suspend fun getAllTeamsWithScores(): List<Pair<Team, Int>> = dbQuery {
        TeamTable.selectAll().map { row ->
            val team = resultRowToTeam(row)
            val score = getTeamScore(team.id)
            team to score
        }
    }

    // get the team name from the player's name associated with the team
    // need iterate through the TeamPlayers table and get the player's name from the Players table
    override suspend fun getTeamName(teamId: Int): String = dbQuery {
        val playerNames = TeamPlayersTable
            .innerJoin(PlayerTable, { playerId }, { PlayerTable.id })
            .slice(PlayerTable.name)
            .select { TeamPlayersTable.teamId eq teamId }
            .map { it[PlayerTable.name] }
            .sorted() // Sort the names alphabetically

        playerNames.joinToString(" ")
    }

    override suspend fun getTeamPlayerCount(teamId: Int): Int = dbQuery {
        TeamPlayersTable.select { TeamPlayersTable.teamId eq teamId }.count().toInt()
    }

    override suspend fun getTeamScore(teamId: Int): Int = dbQuery {
        TennisSetTable.select { (TennisSetTable.teamAId eq teamId) or (TennisSetTable.teamBId eq teamId) }
            .count {
                (it[TennisSetTable.teamAId] == teamId && it[TennisSetTable.teamAScore] > it[TennisSetTable.teamBScore]) ||
                        (it[TennisSetTable.teamBId] == teamId && it[TennisSetTable.teamBScore] > it[TennisSetTable.teamAScore])
            }
    }



    suspend fun getTeamWinningPercentage(teamId: Int): Double = dbQuery {
        val totalMatches = TennisSetTable.select { 
            (TennisSetTable.teamAId eq teamId) or (TennisSetTable.teamBId eq teamId) 
        }.count()

        if (totalMatches.toInt() == 0) return@dbQuery 0.0 // Avoid division by zero

        val wins = getTeamScore(teamId)

        wins.toDouble() / totalMatches * 100 // Return winning percentage
    }

    override suspend fun getAllTeamsWithWinningPercentages(): List<TeamWithNameAndWinningPercentages> = dbQuery {
        TeamTable.selectAll().map { row ->
            val teamId = row[TeamTable.id].value
            val teamName = getTeamName(teamId)
            val playerCount = getTeamPlayerCount(teamId)
            val winningPercentage = getTeamWinningPercentage(teamId)
            TeamWithNameAndWinningPercentages(teamName, playerCount, winningPercentage)
        }
    }

}

val teamRepository: TeamRepository = SQLiteTeamRepository()