package com.caritos.dao

import com.caritos.dao.DatabaseSingleton.dbQuery
import com.caritos.models.Court
import com.caritos.models.Courts
import com.caritos.models.Player
import com.caritos.models.Players
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class DAOPlayerImpl : DAOPlayer {
    private fun resultRowToPlayer(row: ResultRow) = Player(
        id = row[Players.id].value,
        name = row[Players.name],
    )

    override suspend fun getAll(): List<Player> {
        return transaction {
            Players.selectAll().map {
                Player(
                    id = it[Players.id].value,
                    name = it[Players.name],
                )
            }
        }
    }

    override suspend fun get(id: Int): Player? = dbQuery {
       Players
            .select { Players.id eq id }
            .map(::resultRowToPlayer)
            .singleOrNull()
    }

    override suspend fun add(name: String): Player? = dbQuery {
        val insertStatement = Players.insert {
            it[Players.name] = name
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToPlayer)
    }

    override suspend fun edit(id: Int, name: String): Boolean = dbQuery {
        Players.update({ Players.id eq id }) {
            it[Courts.name] = name
        } > 0
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        Players.deleteWhere { Players.id eq id } > 0
    }

}


val daoPlayer: DAOPlayer = DAOPlayerImpl().apply {
    runBlocking {
        if(getAll().isEmpty()) {
            add("Eladio Caritos")
            add("Jason Loy")
            add("Naveen")
            add("David")
        }
    }
}