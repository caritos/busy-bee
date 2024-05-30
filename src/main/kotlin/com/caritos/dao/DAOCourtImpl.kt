package com.caritos.dao

import com.caritos.dao.DatabaseSingleton.dbQuery
import com.caritos.models.Court
import com.caritos.models.Courts
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class DAOCourtImpl : DAOCourt {

    private fun resultRowToCourt(row: ResultRow) = Court(
        id = row[Courts.id].value,
        name = row[Courts.name],
        location = row[Courts.location],
    )

    override suspend fun getAllCourts(): List<Court> {
        return transaction {
            Courts.selectAll().map {
                Court(
                    id = it[Courts.id].value,
                    name = it[Courts.name],
                    location = it[Courts.location]
                )
            }
        }
    }

    override suspend fun court(id: Int): Court? = dbQuery {
        Courts
            .select { Courts.id eq id }
            .map(::resultRowToCourt)
            .singleOrNull()
    }

    override suspend fun addCourt(name: String, location: String): Court? = dbQuery {
        val insertStatement = Courts.insert {
            it[Courts.name] = name
            it[Courts.location] = location
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToCourt)
    }

    override suspend fun editCourt(id: Int, name: String, location: String): Boolean = dbQuery {
        Courts.update({ Courts.id eq id }) {
            it[Courts.name] = name
            it[Courts.location] = location
        } > 0
    }

    override suspend fun deleteCourt(id: Int): Boolean = dbQuery {
        Courts.deleteWhere { Courts.id eq id } > 0
    }
}

val daoCourt: DAOCourt = DAOCourtImpl().apply {
    runBlocking {
        if(getAllCourts().isEmpty()) {
            addCourt("Robert Murphy Junior High School", "Stony Brook")
            addCourt("Stony Brook University", "Stony Brook")
            addCourt("Rocky Point High School", "Rocky Point")
        }
    }
}