package com.caritos.dao

import com.caritos.models.*

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