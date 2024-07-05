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

@Serializable
data class Team(
    val id: Int,
    val name: String, 
    val playerIds: Set<Int>,
)

object Teams : IntIdTable() {
    val name = varchar("name", 255)
    val playerIds = varchar("player_ids", 255)
}