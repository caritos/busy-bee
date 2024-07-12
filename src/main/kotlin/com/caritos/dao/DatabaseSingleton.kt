package com.caritos.dao

import com.caritos.db.CourtTable
import com.caritos.models.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseSingleton {
    fun init() {
        val driverClassName = System.getenv("DB_DRIVER") ?: "org.postgresql.Driver"
        val jdbcURL = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/tennis"
        val username = System.getenv("DB_USER") ?: "user_a"
        val password = System.getenv("DB_PASSWORD") ?: "password_a"
        val database = Database.connect(jdbcURL, driverClassName, username, password)
        
        transaction(database) {
            SchemaUtils.create(TeamPlayers)
            SchemaUtils.create(Users)
            SchemaUtils.create(CourtTable)
            SchemaUtils.create(Players)
            SchemaUtils.create(Matches)
            SchemaUtils.create(Teams)
            SchemaUtils.create(TennisSets)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
