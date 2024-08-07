package com.caritos.busy_bee.plugins

import com.caritos.busy_bee.models.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory


object DatabaseSingleton {
    fun init(config: ApplicationConfig) {
        val logger = LoggerFactory.getLogger("DatabaseSingleton")

        val hikariConfig = HikariConfig().apply {
            driverClassName = "org.sqlite.JDBC"
            jdbcUrl = "jdbc:sqlite:./data/database.db"
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        val dataSource = HikariDataSource(hikariConfig)
        Database.connect(dataSource)

        transaction {
            // Enable foreign key support
            exec("PRAGMA foreign_keys = ON")
            
            SchemaUtils.createMissingTablesAndColumns(TeamPlayersTable)
            SchemaUtils.createMissingTablesAndColumns(UserTable)
            SchemaUtils.createMissingTablesAndColumns(CourtTable)
            SchemaUtils.createMissingTablesAndColumns(PlayerTable)
            SchemaUtils.createMissingTablesAndColumns(MatchTable)
            SchemaUtils.createMissingTablesAndColumns(TeamTable)
            SchemaUtils.createMissingTablesAndColumns(TennisSetTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
        newSuspendedTransaction(Dispatchers.IO, statement = block)
}
