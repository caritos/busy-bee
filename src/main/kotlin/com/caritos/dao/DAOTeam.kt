package com.caritos.dao

import com.caritos.models.*

interface DAOTeam {
    suspend fun getAll(): List<Team>
    suspend fun get(id: Int): Team?
    suspend fun add(playerIds: Set<Int>): Team?
    suspend fun edit(id: Int, playerIds: Set<Int>): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun getOrCreateTeam(playerIds: Set<Int>): Int
    suspend fun getAllSinglePlayerTeamsWithScores(): List<Pair<Team, Int>>
    suspend fun getAllDoublePlayerTeamsWithScores(): List<Pair<Team, Int>>
    suspend fun getTeamScore(teamId: Int): Int
    suspend fun getAllTeamsWithScores(): List<Pair<Team, Int>>
}