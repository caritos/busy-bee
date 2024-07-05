package com.caritos.dao

import com.caritos.dao.DatabaseSingleton.dbQuery
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
        teamAId = row[TennisSets.teamAId],
        teamBId = row[TennisSets.teamBId],
        teamAScore = row[TennisSets.teamAScore],
        teamBScore = row[TennisSets.teamBScore],
    )

    override suspend fun getAll(): List<TennisSet> {
        return transaction {
            TennisSets.selectAll().map {
                TennisSet(
                    id = it[TennisSets.id].value,
                    matchId = it[TennisSets.matchId],
                    setNumber = it[TennisSets.setNumber],
                    teamAId = it[TennisSets.teamAId],
                    teamBId = it[TennisSets.teamBId],
                    teamAScore = it[TennisSets.teamAScore],
                    teamBScore = it[TennisSets.teamBScore],
                )
            }
        }
    }

    override suspend fun get(id: Int): TennisSet? = dbQuery {
        TennisSets.select { TennisSets.id eq id }
            .map(::resultRowToTennisSet)
            .singleOrNull()
    }

    override suspend fun add(matchId: Int, setNumber: Int, teamAId: Int, teamBId: Int, player1Score: Int, player2Score: Int): TennisSet? = dbQuery {
        val insertStatement = TennisSets.insert {
            it[TennisSets.matchId] = matchId
            it[TennisSets.setNumber] = setNumber
            it[TennisSets.teamAId] = teamAId
            it[TennisSets.teamBId] = teamBId
            it[TennisSets.teamAScore] = player1Score
            it[TennisSets.teamBScore] = player2Score
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTennisSet)
    }

    override suspend fun edit(id: Int, matchId: Int, setNumber: Int, teamAId: Int, teamBId: Int, player1Score: Int, player2Score: Int): Boolean = dbQuery {
        TennisSets.update({ TennisSets.id eq id }) {
            it[TennisSets.matchId] = matchId
            it[TennisSets.setNumber] = setNumber
            it[TennisSets.teamAId] = teamAId
            it[TennisSets.teamBId] = teamBId
            it[TennisSets.teamAScore] = player1Score
            it[TennisSets.teamBScore] = player2Score
        } > 0
    }

    override suspend fun getAllForMatch(id: Int): List<TennisSet> = dbQuery {
        TennisSets.select { TennisSets.matchId eq id }
            .map(::resultRowToTennisSet)
    }

    // provides a list of tennis sets for the match
    override suspend fun getTennisSetsForMatch(id: Int): List<TennisSet> {
        return transaction {
            TennisSets.select { TennisSets.matchId eq id }.map {
                TennisSet(
                    id = it[TennisSets.id].value,
                    matchId = it[TennisSets.matchId],
                    setNumber = it[TennisSets.setNumber],
                    teamAId = it[TennisSets.teamAId],
                    teamBId = it[TennisSets.teamBId],
                    teamAScore = it[TennisSets.teamAScore],
                    teamBScore = it[TennisSets.teamBScore],
                )
            }
        }
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        TennisSets.deleteWhere { TennisSets.id eq id } > 0
    }

    override suspend fun getTeamsWithScores(): List<Pair<Int, Int>> = dbQuery {
        TennisSets
            .selectAll()
            .map { it[TennisSets.teamAId] to it[TennisSets.teamAScore] }
            .groupBy { it.first }
            .map { (teamId, scores) -> teamId to scores.sumOf { it.second } }
    }


}

val daoTennisSet : DAOTennisSet = DAOTennisSetImpl().apply {
    runBlocking {
        if(getAll().isEmpty()) {
        }
    }
}