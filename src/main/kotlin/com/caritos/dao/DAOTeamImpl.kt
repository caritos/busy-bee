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
        playerId = row[Teams.playerId],
        matchId = row[Teams.matchId],
    )
    
    override suspend fun getAll(): List<Team> {
        return transaction {
            Teams.selectAll().map {
                Team(
                    id = it[Teams.id].value,
                    playerId = it[Teams.playerId],
                    matchId = it[Teams.matchId],
                )
            }
        }
    }

    override suspend fun get(id: Int): Team? = dbQuery {
       Teams.select { Teams.id eq id }
            .map(::resultRowToTeam)
            .singleOrNull()
    }

    override suspend fun add(playerId: Int, matchId: Int): Team? = dbQuery {
        val insertStatement = Teams.insert {
            it[Teams.playerId] = playerId
            it[Teams.matchId] = matchId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTeam)
    }

    override suspend fun edit(id: Int, playerId: Int, matchId: Int): Boolean = dbQuery {
        Teams.update({ Teams.id eq id }) {
            it[Teams.playerId] = playerId
            it[Teams.matchId] = matchId
        } > 0
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        Teams.deleteWhere { Teams.id eq id } > 0
    }
}


val daoTeam : DAOTeam = DAOTeamImpl().apply {
    runBlocking {
        if(getAll().isEmpty()) {
            add(1, 3)
            add(2, 3)
            add(3, 3)
            add(4, 3)
        }
    }
}