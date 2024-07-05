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

    private fun resultRowToTeam(row: ResultRow) = Team(
        id = row[Teams.id].value,
        name = row[Teams.name],
        playerIds = row[Teams.playerIds].split(",").map { it.toInt() }.toSet(),
    )

    override suspend fun getAll(): List<Team> {
        return transaction {
            Teams.selectAll().map {
                Team(
                    id = it[Teams.id].value,
                    name = it[Teams.name],
                    playerIds = it[Teams.playerIds].split(",").map { it.toInt() }.toSet(),
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
            it[Teams.playerIds] = playerIds.joinToString(",")
            it[Teams.name] = playerNames
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTeam)
    }

    override suspend fun edit(id: Int, playerIds: Set<Int>): Boolean = dbQuery {
        Teams.update({ Teams.id eq id }) {
            it[Teams.playerIds] = playerIds.joinToString(",")
        } > 0
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        Teams.deleteWhere { Teams.id eq id } > 0
    }

    override suspend fun getOrCreateTeam(playerIds: Set<Int>): Int {
        return dbQuery {
            // Sort the player IDs before joining them into a string
            val sortedPlayerIds = playerIds.sorted().joinToString(",")

            // Try to find an existing team with the given player IDs
            val existingTeam = Teams.select { Teams.playerIds eq sortedPlayerIds }.singleOrNull()

            if (existingTeam != null) {
                logger.info("existing team: $existingTeam")
                // If the team exists, return its ID
                existingTeam[Teams.id].value
            } else {
                logger.info("new team: $sortedPlayerIds")
                        // Fetch player names based on playerIds
                val playerNames = Players.select { Players.id inList playerIds }
                            .map { it[Players.name] }
                            .joinToString(" ")
                // If the team doesn't exist, create a new team and return its ID
                val newTeam = Teams.insertAndGetId {
                    it[Teams.playerIds] = sortedPlayerIds
                    it[Teams.name] = playerNames
                }
                newTeam.value
            }
        }
    }

    override suspend fun getAllSinglePlayerTeamsWithScores(): List<Pair<Team, Int>> = dbQuery {
        Teams.selectAll().mapNotNull { row ->
            val team = resultRowToTeam(row)
            val playerCount = team.playerIds.size
            if (playerCount == 1) {
                val score = getTeamScore(team.id)
                team to score
            } else {
                null
            }
        }
    }

    override suspend fun getAllDoublePlayerTeamsWithScores(): List<Pair<Team, Int>> = dbQuery {
        Teams.selectAll().mapNotNull { row ->
            val team = resultRowToTeam(row)
            logger.info(team.toString())
            val playerCount = team.playerIds.size
            if (playerCount == 2) {
                val score = getTeamScore(team.id)
                team to score
            } else {
                null
            }
        }
    }

    override suspend fun getTeamScore(teamId: Int): Int = dbQuery {
        TennisSets.select { (TennisSets.teamAId eq teamId) or (TennisSets.teamBId eq teamId) }
            .map { if (it[TennisSets.teamAId] == teamId) it[TennisSets.teamAScore] else it[TennisSets.teamBScore] }
            .sum()
    }

    override suspend fun getAllTeamsWithScores(): List<Pair<Team, Int>> = dbQuery {
        Teams.selectAll().map { row ->
            val team = resultRowToTeam(row)
            val score = getTeamScore(team.id)
            team to score
        }
    }
}


val daoTeam : DAOTeam = DAOTeamImpl().apply {
    runBlocking {
        if(getAll().isEmpty()) {
        }
    }
}