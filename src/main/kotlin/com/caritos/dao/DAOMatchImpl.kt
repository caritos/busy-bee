package com.caritos.dao

import com.caritos.db.DatabaseSingleton.dbQuery
import com.caritos.models.Match
import com.caritos.models.Matches
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.time.LocalDate

class DAOMatchImpl : DAOMatch {
    val logger = LoggerFactory.getLogger("DAOMatchImpl")

    private fun resultRowToMatch(row: ResultRow) = Match(
        id = row[Matches.id].value,
        date = row[Matches.date],
        courtId = row[Matches.courtId],
        teamAId = row[Matches.teamAId],
        teamBId = row[Matches.teamBId]
    )

    override suspend fun getAll(): List<Match> {
        val logger = LoggerFactory.getLogger("Application")
        logger.info("inside getAll")
        return transaction {
            Matches.selectAll().map {
                Match(
                    id = it[Matches.id].value,
                    date = it[Matches.date],
                    courtId = it[Matches.courtId],
                    teamAId = it[Matches.teamAId],
                    teamBId = it[Matches.teamBId]
                )
            }
        }
    }

    override suspend fun get(id: Int): Match? = dbQuery {
        Matches
            .select { Matches.id eq id }
            .map(::resultRowToMatch)
            .singleOrNull()
    }

    override suspend fun add(
        date: LocalDate,
        courtId: Int,
        teamAId: Int,
        teamBId: Int,
    ): Match? = dbQuery {
        logger.info("adding match")
        val insertStatement = Matches.insert {
            it[Matches.date] = date
            it[Matches.courtId] = courtId
            it[Matches.teamAId] = teamAId
            it[Matches.teamBId] = teamBId
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToMatch)
    }

    override suspend fun edit(
        id: Int,
        date: LocalDate,
        courtId: Int,
        teamAId: Int,
        teamBId: Int,
    ): Boolean = dbQuery {
        Matches.update({ Matches.id eq id }) {
            it[Matches.date] = date
            it[Matches.courtId] = courtId
            it[Matches.teamAId] = teamAId
            it[Matches.teamBId] = teamBId
        } > 0
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        Matches.deleteWhere { Matches.id eq id } > 0
    }


        override suspend fun getRecentMatches(limit: Int): List<Match> = dbQuery {
            Matches
                .selectAll()
                .orderBy(Matches.date to SortOrder.DESC)
                .limit(limit)
                .map(::resultRowToMatch)
        }
}


val daoMatch: DAOMatch = DAOMatchImpl().apply {
    runBlocking {
        if(getAll().isEmpty()) {
//            add(LocalDateTime.now(), 1,1,3, true)
        }
    }
}