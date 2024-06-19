package com.caritos.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

data class Match(
    val id: Int,
    val date: LocalDateTime,
    val courtId: Int,
    val teamAId: Int,
    val teamBId: Int,
)

data class MatchWithPlayerNames(
    val id: Int,
    val date: LocalDateTime,
    val courtId: Int,
    val courtName: String,
    val teamAId: String,
    val teamANames: String,
    val teamBId: String,
    val teamBNames: String,
    val score: List<TennisSet>
)

object Matches : IntIdTable() {
    val date = datetime("date")
    val courtId = integer("court_id").references(Courts.id)
    val teamAId = integer("team_a_id").references(Teams.id)
    val teamBId = integer("team_b_id").references(Teams.id)
}

