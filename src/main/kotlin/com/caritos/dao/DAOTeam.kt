package com.caritos.dao

import com.caritos.models.*

interface DAOTeam {
    suspend fun getAll(): List<Team>
    suspend fun get(id: Int): Team?
    suspend fun add(playerId: Int, matchId: Int): Team?
    suspend fun edit(id: Int, playerId: Int, matchId: Int): Boolean
    suspend fun delete(id: Int): Boolean
}