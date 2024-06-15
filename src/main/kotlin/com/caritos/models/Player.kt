package com.caritos.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Player(
    val id: Int,
    val name: String
)

object Players : IntIdTable() {
    val name = varchar("name", 50)
}