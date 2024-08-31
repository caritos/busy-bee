package com.caritos.busy_bee.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Team(
    val id: Int,
    val name: String,
)

interface TeamRepository {
    suspend fun allTeams(): List<Team>
    suspend fun teamById(id: Int): Team?
    suspend fun addTeam(playerIds: Set<Int>): Team?
    suspend fun updateTeam(id: Int, name: String): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun createTeam(name: String, playerIds: Set<Int>): Int
    suspend fun getTeamScore(teamId: Int): Int
    suspend fun getAllTeamsWithScores(): List<Pair<Team, Int>>
    suspend fun getTeamName(teamId: Int): String
    suspend fun getTeamPlayerCount(teamId: Int): Int
    suspend fun getTeamsWithNameAndScore(): List<TeamWithNameAndScore>
    suspend fun getAllTeamsWithWinningPercentages(): List<TeamWithNameAndWinningPercentages>
}

@Serializable
data class TeamWithNameAndScore(
    val id: Int,
    val name: String,
    val numberOfPlayers: Int,
    val score: Int
)

@Serializable
data class TeamWithNameAndWinningPercentages(
    val name: String,
    val numberOfPlayers: Int,
    val rate: Double
)

object TeamTable : IntIdTable("teams") {
    val name = varchar("name", 255).default("")
}

class TeamDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TeamDAO>(TeamTable)
    var name by TeamTable.name
}

fun daoToModel(dao: TeamDAO) = Team(
    id = dao.id.value,
    name = dao.name,
)