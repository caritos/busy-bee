package com.caritos.dao

import com.caritos.models.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*

object DatabaseSingleton {
    fun init() {
        val driverClassName = "org.postgresql.Driver"
        val jdbcURL = "jdbc:postgresql://localhost:5432/tennis"
        val username = "user_a"
        val password = "password_a"
        val database = Database.connect(jdbcURL, driverClassName, username, password)
        transaction(database) {
            SchemaUtils.create(Users)
            SchemaUtils.create(Articles)
            SchemaUtils.create(Courts)
            SchemaUtils.create(Players)
            SchemaUtils.create(Matches)
            SchemaUtils.create(Teams)
            SchemaUtils.create(TennisSets)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
