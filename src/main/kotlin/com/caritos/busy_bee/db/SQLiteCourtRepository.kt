package com.caritos.busy_bee.db

import com.caritos.busy_bee.models.*
import com.caritos.busy_bee.plugins.DatabaseSingleton.suspendTransaction
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update

class SQLiteCourtRepository: CourtRepository {

    private fun resultRowToCourt(row: ResultRow) = Court(
        id = row[CourtTable.id].value,
        name = row[CourtTable.name],
        location = row[CourtTable.location]
    )

    override suspend fun allCourts(): List<Court> = suspendTransaction {
        CourtDAO.all().map(::daoToModel)
    }

    override suspend fun courtById(id: Int): Court? = suspendTransaction {
        CourtDAO
            .find { (CourtTable.id eq id) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun courtByName(name: String): Court? = suspendTransaction {
        CourtDAO
            .find { (CourtTable.name eq name) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun courtsByLocation(location: String): List<Court> = suspendTransaction {
        CourtDAO
            .find { (CourtTable.location eq location) }
            .map(::daoToModel)
    }

    override suspend fun addCourt(court_name: String, court_location: String): Court? = suspendTransaction {
        val insertStatement = CourtTable.insert {
            it[name] = court_name
            it[location] = court_location
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToCourt)
    }

    override suspend fun updateCourt(id: Int, name: String, location: String): Unit = suspendTransaction {
        CourtTable.update({ CourtTable.id eq id }) {
            it[CourtTable.name] = name
            it[CourtTable.location] = location
        }
    }

    override suspend fun removeCourt(id: Int): Boolean = suspendTransaction {
        val deletedRows = CourtTable.deleteWhere { CourtTable.id eq id }
        deletedRows > 0
    }

}

val courtRepository : CourtRepository = SQLiteCourtRepository()