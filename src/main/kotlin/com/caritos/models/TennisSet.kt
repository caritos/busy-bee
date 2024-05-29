package com.caritos.models

import org.jetbrains.exposed.dao.id.IntIdTable

data class TennisSet(
    val id: Int,
    val matchId: Int,
    val setNumber: Int,
    val player1Score: Int,
    val player2Score: Int
)

object TennisSets : IntIdTable() {
    val matchId = integer("match_id").references(Matches.id)
    val setNumber = integer("set_number")
    val player1Score = integer("player1_score")
    val player2Score = integer("player2_score")
}

