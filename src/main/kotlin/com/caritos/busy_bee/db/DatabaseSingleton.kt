package com.caritos.busy_bee.db

import io.ktor.server.config.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.*
import org.slf4j.LoggerFactory

object DatabaseSingleton {
    fun init(config: ApplicationConfig) {
        val logger = LoggerFactory.getLogger("DatabaseConfig")

        val dbUrl = config.propertyOrNull("ktor.storage.jdbcURL")?.getString()
        val dbDriver = config.propertyOrNull("ktor.storage.driver")?.getString()
        val dbUser = config.propertyOrNull("ktor.storage.user")?.getString()
        val dbPassword = config.propertyOrNull("ktor.storage.password")?.getString()

        if (dbUrl == null || dbDriver == null || dbUser == null || dbPassword == null) {
            logger.error("Database configuration is missing")
            throw ApplicationConfigurationException("Database configuration is missing")
        }

        logger.info("Database URL: $dbUrl")
        logger.info("Database Driver: $dbDriver")
        logger.info("Database User: $dbUser")

        val database = Database.connect(url = dbUrl, driver = dbDriver, user = dbUser, password = dbPassword)

        transaction(database) {
            SchemaUtils.create(TeamPlayersTable)
            SchemaUtils.create(UserTable)
            SchemaUtils.create(CourtTable)
            SchemaUtils.create(PlayerTable)
            SchemaUtils.create(MatchTable)
            SchemaUtils.create(TeamTable)
            SchemaUtils.create(TennisSetTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
