package com.caritos.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*

data class User(val id: Int, val username: String, val password: String, val salt: String)

object Users: IntIdTable() {
    val username = varchar("username", 50).uniqueIndex()
    val password = varchar("password", 60) // bcrypt hashed password
    val salt = varchar("salt", 32)
}