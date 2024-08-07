package com.caritos.busy_bee.db

import com.caritos.busy_bee.plugins.DatabaseSingleton.dbQuery
import com.caritos.busy_bee.models.TennisSet
import com.caritos.busy_bee.models.TennisSetRepository
import com.caritos.busy_bee.models.TennisSetTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class SQLiteTennisSetRepository: TennisSetRepository {
    private fun resultRowToTennisSet(row: ResultRow) = TennisSet(
        id = row[TennisSetTable.id].value,
        matchId = row[TennisSetTable.matchId],
        setNumber = row[TennisSetTable.setNumber],
        teamAId = row[TennisSetTable.teamAId],
        teamBId = row[TennisSetTable.teamBId],
        teamAScore = row[TennisSetTable.teamAScore],
        teamBScore = row[TennisSetTable.teamBScore],
    )

    override suspend fun getAll(): List<TennisSet> {
        return transaction {
            TennisSetTable.selectAll().map {
                TennisSet(
                    id = it[TennisSetTable.id].value,
                    matchId = it[TennisSetTable.matchId],
                    setNumber = it[TennisSetTable.setNumber],
                    teamAId = it[TennisSetTable.teamAId],
                    teamBId = it[TennisSetTable.teamBId],
                    teamAScore = it[TennisSetTable.teamAScore],
                    teamBScore = it[TennisSetTable.teamBScore],
                )
            }
        }
    }

    override suspend fun get(id: Int): TennisSet? = dbQuery {
        TennisSetTable.select { TennisSetTable.id eq id }
            .map(::resultRowToTennisSet)
            .singleOrNull()
    }

    override suspend fun add(matchId: Int, setNumber: Int, teamAId: Int, teamBId: Int, player1Score: Int, player2Score: Int): TennisSet? = dbQuery {
        val insertStatement = TennisSetTable.insert {
            it[TennisSetTable.matchId] = matchId
            it[TennisSetTable.setNumber] = setNumber
            it[TennisSetTable.teamAId] = teamAId
            it[TennisSetTable.teamBId] = teamBId
            it[teamAScore] = player1Score
            it[teamBScore] = player2Score
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTennisSet)
    }

    override suspend fun edit(id: Int, matchId: Int, setNumber: Int, teamAId: Int, teamBId: Int, player1Score: Int, player2Score: Int): Boolean = dbQuery {
        TennisSetTable.update({ TennisSetTable.id eq id }) {
            it[TennisSetTable.matchId] = matchId
            it[TennisSetTable.setNumber] = setNumber
            it[TennisSetTable.teamAId] = teamAId
            it[TennisSetTable.teamBId] = teamBId
            it[teamAScore] = player1Score
            it[teamBScore] = player2Score
        } > 0
    }

    override suspend fun getAllForMatch(id: Int): List<TennisSet> = dbQuery {
        TennisSetTable.select { TennisSetTable.matchId eq id }
            .map(::resultRowToTennisSet)
    }

    // provides a list of tennis sets for the match
    override suspend fun getTennisSetsForMatch(id: Int): List<TennisSet> {
        return transaction {
            TennisSetTable.select { TennisSetTable.matchId eq id }.map {
                TennisSet(
                    id = it[TennisSetTable.id].value,
                    matchId = it[TennisSetTable.matchId],
                    setNumber = it[TennisSetTable.setNumber],
                    teamAId = it[TennisSetTable.teamAId],
                    teamBId = it[TennisSetTable.teamBId],
                    teamAScore = it[TennisSetTable.teamAScore],
                    teamBScore = it[TennisSetTable.teamBScore],
                )
            }
        }
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        TennisSetTable.deleteWhere { TennisSetTable.id eq id } > 0
    }
}

val daoTennisSet : TennisSetRepository = SQLiteTennisSetRepository()