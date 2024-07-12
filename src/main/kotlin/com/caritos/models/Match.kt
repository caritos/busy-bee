package com.caritos.models

import com.caritos.db.CourtTable
import com.caritos.db.TeamTable
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
    val courtId = integer("court_id").references(CourtTable.id)
    val teamAId = integer("team_a_id").references(TeamTable.id)
    val teamBId = integer("team_b_id").references(TeamTable.id)
}

