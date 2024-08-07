package com.caritos.busy_bee.db

import com.caritos.busy_bee.plugins.DatabaseSingleton.suspendTransaction
import com.caritos.busy_bee.models.*
import com.caritos.busy_bee.plugins.DatabaseSingleton.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.update
import java.time.LocalDate

class SQLitePlayerRepository: PlayerRepository {

    private fun resultRowToPlayer(row: ResultRow) = Player(
        id = row[PlayerTable.id].value,
        name = row[PlayerTable.name]
    )

    override suspend fun allPlayers(): List<Player> = suspendTransaction {
        PlayerDAO.all().map(::daoToModel)
    }

    override suspend fun playerById(id: Int): Player? = suspendTransaction {
        PlayerDAO
            .find { (PlayerTable.id eq id) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun playerByName(name: String): Player? = suspendTransaction {
        PlayerDAO
            .find { (PlayerTable.name eq name) }
            .limit(1)
            .map(::daoToModel)
            .firstOrNull()
    }

    override suspend fun addPlayer(name: String): Player? = suspendTransaction {
        val insertStatement = PlayerTable.insert {
            it[PlayerTable.name] = name
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToPlayer)
    }

    override suspend fun updatePlayer(id: Int, name: String): Unit = suspendTransaction {
        PlayerTable.update({ PlayerTable.id eq id }) {
            it[PlayerTable.name] = name
        }
    }

    override suspend fun removePlayer(id: Int): Boolean = suspendTransaction {
        val deletedRows = PlayerTable.deleteWhere { CourtTable.id eq id }
        deletedRows > 0
    }

}

val playerRepository: PlayerRepository = SQLitePlayerRepository()