package com.caritos.dao

import com.caritos.dao.DatabaseSingleton.dbQuery
import com.caritos.models.Courts
import com.caritos.models.Match
import com.caritos.models.Matches
import com.caritos.models.Players
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

class DAOMatchImpl : DAOMatch {
    val logger = LoggerFactory.getLogger("DAOMatchImpl")

    private fun resultRowToMatch(row: ResultRow) = Match(
        id = row[Matches.id].value,
        date = row[Matches.date],
        courtId = row[Matches.courtId],
        winnerId = row[Matches.winnerId],
        loserId = row[Matches.loserId],
        isDoubles = row[Matches.isDoubles]
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
                    winnerId = it[Matches.winnerId],
                    loserId = it[Matches.loserId],
                    isDoubles = it[Matches.isDoubles]
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
        date: LocalDateTime,
        courtId: Int,
        winnerId: Int,
        loserId: Int,
        isDoubles: Boolean
    ): Match? = dbQuery {
        logger.info("adding match")
        val insertStatement = Matches.insert {
            it[Matches.date] = date
            it[Matches.courtId] = courtId
            it[Matches.winnerId] = winnerId
            it[Matches.loserId] = loserId
            it[Matches.isDoubles] = isDoubles
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToMatch)
    }

    override suspend fun edit(
        id: Int,
        date: LocalDateTime,
        courtId: Int,
        winnerId: Int,
        loserId: Int,
        isDoubles: Boolean
    ): Boolean = dbQuery {
        Matches.update({ Matches.id eq id }) {
            it[Matches.date] = date
            it[Matches.courtId] = courtId
            it[Matches.winnerId] = winnerId
            it[Matches.loserId] = loserId
            it[Matches.isDoubles] = isDoubles
        } > 0
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        Matches.deleteWhere { Matches.id eq id } > 0
    }
}


val daoMatch: DAOMatch = DAOMatchImpl().apply {
    runBlocking {
        if(getAll().isEmpty()) {
            add(LocalDateTime.now(), 1,1,3, true)
        }
    }
}