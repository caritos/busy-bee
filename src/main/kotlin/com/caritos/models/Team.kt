package com.caritos.models

import org.jetbrains.exposed.dao.id.IntIdTable

data class Team(
    val id: Int,
    val playerId: Int,
    val matchId: Int
)

object Teams : IntIdTable() {
    val playerId = integer("player_id").references(Players.id)
    val matchId = integer("match_id").references(Matches.id)
}