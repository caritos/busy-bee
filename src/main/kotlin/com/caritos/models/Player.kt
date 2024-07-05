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
    val name = varchar("name", 255).default("")
    val playerIds = varchar("player_ids", 255).default("")
}

@Serializable
data class TeamPlayer(
    val playerId: Int,
    val teamId: Int
)   

object TeamPlayers : IntIdTable() {
    val playerId = integer("player_id").references(Players.id)
    val teamId = integer("team_id").references(Teams.id)
}
