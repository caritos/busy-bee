package com.caritos.busy_bee.models

import com.caritos.busy_bee.db.*
import com.caritos.busy_bee.db.DatabaseSingleton.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.time.LocalDate

class PostgresMatchRepository: MatchRepository {
    val logger = LoggerFactory.getLogger("DAOMatchImpl")

    private fun resultRowToMatch(row: ResultRow) = Match(
        id = row[MatchTable.id].value,
        date = row[MatchTable.date],
        courtId = row[MatchTable.courtId],
        teamAId = row[MatchTable.teamAId],
        teamBId = row[MatchTable.teamBId]
    )

    override suspend fun getAll(): List<Match> {
        val logger = LoggerFactory.getLogger("Application")
        logger.info("inside getAll")
        return transaction {
            MatchTable.selectAll().map {
                Match(
                    id = it[MatchTable.id].value,
                    date = it[MatchTable.date],
                    courtId = it[MatchTable.courtId],
                    teamAId = it[MatchTable.teamAId],
                    teamBId = it[MatchTable.teamBId]
                )
            }
        }
    }

    override suspend fun get(id: Int): Match? = dbQuery {
        MatchTable
            .select { MatchTable.id eq id }
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
        val insertStatement = MatchTable.insert {
            it[MatchTable.date] = date
            it[MatchTable.courtId] = courtId
            it[MatchTable.teamAId] = teamAId
            it[MatchTable.teamBId] = teamBId
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
        MatchTable.update({ MatchTable.id eq id }) {
            it[MatchTable.date] = date
            it[MatchTable.courtId] = courtId
            it[MatchTable.teamAId] = teamAId
            it[MatchTable.teamBId] = teamBId
        } > 0
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        MatchTable.deleteWhere { MatchTable.id eq id } > 0
    }


    override suspend fun getRecentMatches(limit: Int): List<Match> = dbQuery {
        MatchTable
            .selectAll()
            .orderBy(MatchTable.date to SortOrder.DESC)
            .limit(limit)
            .map(::resultRowToMatch)
    }
}

val matchRepository: MatchRepository = PostgresMatchRepository()