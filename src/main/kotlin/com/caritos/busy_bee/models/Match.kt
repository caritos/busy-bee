package com.caritos.busy_bee.models

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

interface MatchRepository {
    suspend fun getAll(): List<Match>
    suspend fun get(id: Int): Match?
    suspend fun add(date: LocalDate, courtId: Int, teamAId: Int, teamBId: Int): Match?
    suspend fun edit(id: Int, date: LocalDate, courtId: Int, teamAId: Int, teamBId: Int): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun getRecentMatches(limit: Int): List<Match>
}

object MatchTable : IntIdTable("matches") {
    val date = date("date")
    val courtId = integer("court_id").references(CourtTable.id, onDelete = ReferenceOption.CASCADE)
    val teamAId = integer("team_a_id").references(TeamTable.id, onDelete = ReferenceOption.CASCADE)
    val teamBId = integer("team_b_id").references(TeamTable.id, onDelete = ReferenceOption.CASCADE)
}