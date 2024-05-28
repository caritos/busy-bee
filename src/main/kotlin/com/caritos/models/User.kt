package com.caritos.models

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

data class User(val id: Int, val username: String, val password: String, val salt: String)
data class Court(
    val id: EntityID<Int>,
    val name: String,
    val location: String
)

data class Player(
    val id: Int,
    val name: String
)
data class Team(
    val id: Int,
    val playerId: Int,
    val matchId: Int
)
data class TennisSet(
    val id: Int,
    val matchId: Int,
    val setNumber: Int,
    val player1Score: Int,
    val player2Score: Int
)
data class Match(
    val id: Int,
    val date: LocalDateTime,
    val courtId: Int,
    val winnerId: Int,
    val loserId: Int,
    val isDoubles: Boolean
)


object Users: IntIdTable() {
    val username = varchar("username", 50).uniqueIndex()
    val password = varchar("password", 60) // bcrypt hashed password
    val salt = varchar("salt", 32)
}

object Courts : IntIdTable() {
    val name = varchar("name", 100).uniqueIndex()
    val location = varchar("location", 255)
}

object Players : IntIdTable() {
    val name = varchar("name", 50)
}

object Matches : IntIdTable() {
    val date = datetime("date")
    val courtId = integer("court_id").references(Courts.id)
    val winnerId = integer("winner_id").references(Players.id)
    val loserId = integer("loser_id").references(Players.id)
    val isDoubles = bool("is_doubles")
}

object Teams : IntIdTable() {
    val playerId = integer("player_id").references(Players.id)
    val matchId = integer("match_id").references(Matches.id)
}

object TennisSets : IntIdTable() {
    val matchId = integer("match_id").references(Matches.id)
    val setNumber = integer("set_number")
    val player1Score = integer("player1_score")
    val player2Score = integer("player2_score")
}

