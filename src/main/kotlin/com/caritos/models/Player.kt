package com.caritos.models

import org.jetbrains.exposed.dao.id.IntIdTable

data class Player(
    val id: Int,
    val name: String
)

object Players : IntIdTable() {
    val name = varchar("name", 50)
}