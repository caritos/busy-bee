package com.caritos.models

import org.jetbrains.exposed.dao.id.IntIdTable

data class Team(
    val id: Int,
    val playerIds: Set<Int>,
)

object Teams : IntIdTable() {
    val playerIds = varchar("player_ids", 255)
}