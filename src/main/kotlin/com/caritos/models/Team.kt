package com.caritos.models

import org.jetbrains.exposed.dao.id.IntIdTable

data class Team(
    val id: Int,
    val name: String, 
    val playerIds: Set<Int>,
)

object Teams : IntIdTable() {
    val name = varchar("name", 255)
    val playerIds = varchar("player_ids", 255)
}