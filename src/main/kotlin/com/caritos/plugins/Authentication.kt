package com.caritos.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory
import java.security.SecureRandom
import java.util.*
import at.favre.lib.crypto.bcrypt.BCrypt
import com.caritos.models.User
import com.caritos.models.Users
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun generateSalt(): String {
    val random = SecureRandom()
    val salt = ByteArray(16)
    random.nextBytes(salt)
    return Base64.getEncoder().encodeToString(salt)
}

fun hashPassword(password: String, salt: String): String {
    return BCrypt.withDefaults().hashToString(12, (salt + password).toCharArray())
}

fun verifyPassword(password: String, salt: String, hashedPassword: String): Boolean {
    val result = BCrypt.verifyer().verify((salt + password).toCharArray(), hashedPassword)
    return result.verified
}
fun Application.configureAuthentication() {
    val log = LoggerFactory.getLogger("Application")
    install(Authentication) {
        form("auth-form") {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                val user = transaction {
                    Users.select { Users.username eq credentials.name }.singleOrNull()?.let {
                        User(it[Users.id].value, it[Users.username], it[Users.password], it[Users.salt])
                    }
                }
                if (user != null && verifyPassword(credentials.password, user.salt, user.password)) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
        session<UserSession>("auth-session") {
            validate { session ->
                log.debug("Validating session for user: ${session.username}")
                if (session.username.isNotBlank()) {
                    session
                } else {
                    null
                }
            }
            challenge {
                log.info("Redirecting to login due to missing session")
                call.respondRedirect("/login")
            }
        }
    }
}