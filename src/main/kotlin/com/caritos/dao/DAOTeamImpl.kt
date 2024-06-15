package com.caritos.dao

import com.caritos.dao.DatabaseSingleton.dbQuery
import com.caritos.models.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class DAOTeamImpl : DAOTeam {
    private fun resultRowToTeam(row: ResultRow) = Team(
        id = row[Teams.id].value,
        playerIds = row[Teams.playerIds].split(",").map { it.toInt() }.toSet(),
    )

    override suspend fun getAll(): List<Team> {
        return transaction {
            Teams.selectAll().map {
                Team(
                    id = it[Teams.id].value,
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
        val insertStatement = Teams.insert {
            it[Teams.playerIds] = playerIds.joinToString(",")
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
            // Try to find an existing team with the given player IDs
            val existingTeam = Teams.select { Teams.playerIds eq playerIds.joinToString(",") }.singleOrNull()

            if (existingTeam != null) {
                // If the team exists, return its ID
                existingTeam[Teams.id].value
            } else {
                // If the team doesn't exist, create a new team and return its ID
                val newTeam = Teams.insertAndGetId {
                    it[Teams.playerIds] = playerIds.joinToString(",") // Assuming the first player ID should be used if the team doesn't exist
                }
                newTeam.value
            }
        }
    }
}


val daoTeam : DAOTeam = DAOTeamImpl().apply {
    runBlocking {
        if(getAll().isEmpty()) {
        }
    }
}