package com.caritos.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

data class Match(
    val id: Int,
    val date: LocalDateTime,
    val courtId: Int,
    val winnerId: Int,
    val loserId: Int,
    val isDoubles: Boolean
)

object Matches : IntIdTable() {
    val date = datetime("date")
    val courtId = integer("court_id").references(Courts.id)
    val winnerId = integer("winner_id").references(Players.id)
    val loserId = integer("loser_id").references(Players.id)
    val isDoubles = bool("is_doubles")
}

