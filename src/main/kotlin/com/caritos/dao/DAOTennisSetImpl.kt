package com.caritos.dao

import com.caritos.dao.DatabaseSingleton.dbQuery
import com.caritos.models.Match
import com.caritos.models.TennisSet
import com.caritos.models.TennisSets
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

/**
 *     val id: Int,
 *     val matchId: Int,
 *     val setNumber: Int,
 *     val player1Score: Int,
 *     val player2Score: Int
 */
class DAOTennisSetImpl : DAOTennisSet {
    private fun resultRowToTennisSet(row: ResultRow) = TennisSet(
        id = row[TennisSets.id].value,
        matchId = row[TennisSets.matchId],
        setNumber = row[TennisSets.setNumber],
        player1Score = row[TennisSets.player1Score],
        player2Score = row[TennisSets.player2Score],
    )

    override suspend fun getAll(): List<TennisSet> {
        return transaction {
            TennisSets.selectAll().map {
                TennisSet(
                    id = it[TennisSets.id].value,
                    matchId = it[TennisSets.matchId],
                    setNumber = it[TennisSets.setNumber],
                    player1Score = it[TennisSets.player1Score],
                    player2Score = it[TennisSets.player2Score],
                )
            }
        }
    }

    override suspend fun get(id: Int): TennisSet? = dbQuery {
        TennisSets.select { TennisSets.id eq id }
            .map(::resultRowToTennisSet)
            .singleOrNull()
    }

    override suspend fun add(matchId: Int, setNumber: Int, player1Score: Int, player2Score: Int): TennisSet? = dbQuery {
        val insertStatement = TennisSets.insert {
            it[TennisSets.matchId] = matchId
            it[TennisSets.setNumber] = setNumber
            it[TennisSets.player1Score] = player1Score
            it[TennisSets.player2Score] = player2Score
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTennisSet)
    }

    override suspend fun edit(id: Int, matchId: Int, setNumber: Int, player1Score: Int, player2Score: Int): Boolean = dbQuery {
        TennisSets.update({ TennisSets.id eq id }) {
            it[TennisSets.matchId] = matchId
            it[TennisSets.setNumber] = setNumber
            it[TennisSets.player1Score] = player1Score
            it[TennisSets.player2Score] = player2Score
        } > 0
    }

    override suspend fun getAllForMatch(id: Int): List<TennisSet> = dbQuery {
        TennisSets.select { TennisSets.matchId eq id }
            .map(::resultRowToTennisSet)
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        TennisSets.deleteWhere { TennisSets.id eq id } > 0
    }
}

val daoTennisSet : DAOTennisSet = DAOTennisSetImpl().apply {
    runBlocking {
        if(getAll().isEmpty()) {
            add(3,1,6,4)
            add(3,2,6,4)
            add(3,3,0,4)
        }
    }
}