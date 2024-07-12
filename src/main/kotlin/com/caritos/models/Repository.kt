package com.caritos.models

import kotlinx.serialization.Serializable

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

interface DAOTennisSet {
    suspend fun getAll(): List<TennisSet>
    suspend fun get(id: Int): TennisSet?
    suspend fun add(matchId: Int, setNumber: Int, teamAId: Int, teamBId: Int, teamAScore: Int, teamBScore: Int): TennisSet?
    suspend fun edit(id: Int, matchId: Int, setNumber: Int, teamAId: Int, teamBId: Int, teamAScore: Int, teamBScore: Int): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun getAllForMatch(id: Int): List<TennisSet>
    suspend fun getTennisSetsForMatch(id: Int): List<TennisSet>
}

@Serializable
data class Court(
    val id: Int,
    val name: String,
    val location: String
)

@Serializable
data class Player(
    val id: Int,
    val name: String
)

@Serializable
data class Team(
    val id: Int,
    val name: String,
)

interface DAOTeam {
    suspend fun getAll(): List<Team>
    suspend fun get(id: Int): Team?
    suspend fun add(playerIds: Set<Int>): Team?
    suspend fun edit(id: Int, name: String): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun createTeam(name: String, playerIds: Set<Int>): Int
    suspend fun getTeamScore(teamId: Int): Int
    suspend fun getAllTeamsWithScores(): List<Pair<Team, Int>>
    suspend fun getTeamName(teamId: Int): String
    suspend fun getTeamPlayerCount(teamId: Int): Int
    suspend fun getTeamsWithNameAndScore(): List<TeamWithNameAndScore>
}

@Serializable
data class TeamWithNameAndScore(
    val id: Int,
    val name: String,
    val numberOfPlayers: Int,
    val score: Int
)

@Serializable
data class TeamPlayer(
    val playerId: Int,
    val teamId: Int
)

interface CourtRepository {
    suspend fun allCourts(): List<Court>
    suspend fun courtById(id: Int): Court?
    suspend fun courtByName(name: String): Court?
    suspend fun courtsByLocation(name: String): List<Court>
    suspend fun addCourt(name: String, location: String): Court?
    suspend fun updateCourt(id: Int, name: String, location: String) 
    suspend fun removeCourt(id: Int): Boolean
}

interface PlayerRepository {
    suspend fun allPlayers(): List<Player>
    suspend fun playerById(id: Int): Player?
    suspend fun playerByName(name: String): Player?
    suspend fun addPlayer(name: String): Player?
    suspend fun updatePlayer(id: Int, name: String)
    suspend fun removePlayer(id: Int): Boolean
}