package com.caritos.busy_bee.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class TennisSet(
    val id: Int,
    val matchId: Int,
    val setNumber: Int,
    val teamAId: Int,
    val teamBId: Int,
    val teamAScore: Int,
    val teamBScore: Int
)

interface TennisSetRepository {
    suspend fun getAll(): List<TennisSet>
    suspend fun get(id: Int): TennisSet?
    suspend fun add(matchId: Int, setNumber: Int, teamAId: Int, teamBId: Int, teamAScore: Int, teamBScore: Int): TennisSet?
    suspend fun edit(id: Int, matchId: Int, setNumber: Int, teamAId: Int, teamBId: Int, teamAScore: Int, teamBScore: Int): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun getAllForMatch(id: Int): List<TennisSet>
    suspend fun getTennisSetsForMatch(id: Int): List<TennisSet>
}

object TennisSetTable : IntIdTable("tennissets") {
    val matchId = integer("match_id").references(MatchTable.id, onDelete = ReferenceOption.CASCADE)
    val setNumber = integer("set_number")
    val teamAId = integer("team_a_id").references(TeamTable.id, onDelete = ReferenceOption.CASCADE)
    val teamBId = integer("team_b_id").references(TeamTable.id, onDelete = ReferenceOption.CASCADE)
    val teamAScore = integer("team_a_score")
    val teamBScore = integer("team_b_score")
}