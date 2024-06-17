package com.caritos.models

import org.jetbrains.exposed.dao.id.IntIdTable

data class TennisSet(
    val id: Int,
    val matchId: Int,
    val setNumber: Int,
    val teamAId: Int,
    val teamBId: Int,
    val teamAScore: Int,
    val teamBScore: Int
)

object TennisSets : IntIdTable() {
    val matchId = integer("match_id").references(Matches.id)
    val setNumber = integer("set_number")
    val teamAId = integer("team_a_id").references(Teams.id)
    val teamBId = integer("team_b_id").references(Teams.id)
    val teamAScore = integer("team_a_score")
    val teamBScore = integer("team_b_score")
}

