package com.caritos.models

import com.caritos.db.CourtDAO
import com.caritos.db.CourtTable
import com.caritos.db.daoToModel
import com.caritos.db.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update

class PostgresCourtRepository: CourtRepository {
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
        val newCourt = CourtDAO.new {
            name = court_name
            location = court_location
        }
        daoToModel(newCourt)
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

val courtRepository : CourtRepository = PostgresCourtRepository()