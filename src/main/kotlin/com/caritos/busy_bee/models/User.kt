package com.caritos.busy_bee.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class User(
    val id: Int,
    val username: String,
    val password: String,
    val salt: String
)

object UserTable: IntIdTable("users") {
    val username = varchar("username", 50).uniqueIndex()
    val password = varchar("password", 60) // bcrypt hashed password
    val salt = varchar("salt", 250)
}