package com.caritos.models

import com.caritos.db.DatabaseSingleton.dbQuery
import com.caritos.db.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

class PostgresTeamRepository: TeamRepository {
    val logger = LoggerFactory.getLogger("DAOTeamImpl")

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

    override suspend fun getAll(): List<Team> {
        return transaction {
            TeamTable.selectAll().map {
                Team(
                    id = it[TeamTable.id].value,
                    name = it[TeamTable.name],
                )
            }
        }
    }

    override suspend fun teamById(id: Int): Team? = suspendTransaction{
        TeamDAO
            .find { ( TeamTable.id eq id) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun add(playerIds: Set<Int>): Team? = dbQuery {
        // Fetch player names based on playerIds
        val playerNames = PlayerTable.select { PlayerTable.id inList playerIds }
            .map { it[PlayerTable.name] }
            .joinToString(" ")
        val insertStatement = TeamTable.insert {
            it[TeamTable.name] = playerNames
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTeam)
    }

    override suspend fun edit(id: Int, name: String): Boolean = dbQuery {
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
            .innerJoin(PlayerTable, { TeamPlayersTable.playerId }, { PlayerTable.id })
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


}

val teamRepository: TeamRepository = PostgresTeamRepository()