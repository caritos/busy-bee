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
)

data class TeamWithNameAndScore(
    val id: Int,
    val name: String,
    val numberOfPlayers: Int,
    val score: Int
)

object Teams : IntIdTable() {
    val name = varchar("name", 255).default("")
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
