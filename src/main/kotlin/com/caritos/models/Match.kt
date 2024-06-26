package com.caritos.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

data class Match(
    val id: Int,
    val date: LocalDate,
    val courtId: Int,
    val teamAId: Int,
    val teamBId: Int,
)

data class MatchWithPlayerNames(
    val id: Int,
    val date: LocalDate,
    val courtId: Int,
    val courtName: String,
    val teamAId: String,
    val teamANames: String,
    val teamBId: String,
    val teamBNames: String,
    val score: List<TennisSet>
)

object Matches : IntIdTable() {
    val date = date("date")
    val courtId = integer("court_id").references(Courts.id)
    val teamAId = integer("team_a_id").references(Teams.id)
    val teamBId = integer("team_b_id").references(Teams.id)
}

