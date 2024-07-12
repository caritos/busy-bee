package com.caritos.models

import com.caritos.db.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update

class PostgresPlayerRepository: PlayerRepository {
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

    override suspend fun addPlayer(player_name: String): Player? = suspendTransaction {
        val newPlayer = PlayerDAO.new {
            name = player_name
        }
        daoToModel(newPlayer)
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

val playerRepository: PlayerRepository = PostgresPlayerRepository()