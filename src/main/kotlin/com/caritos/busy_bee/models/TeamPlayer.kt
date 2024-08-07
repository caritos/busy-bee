package com.caritos.busy_bee.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class TeamPlayer(
    val playerId: Int,
    val teamId: Int
)

object TeamPlayersTable : IntIdTable("teamplayers") {
    val playerId = integer("player_id").references(PlayerTable.id, onDelete = ReferenceOption.CASCADE)
    val teamId = integer("team_id").references(TeamTable.id, onDelete = ReferenceOption.CASCADE)
}