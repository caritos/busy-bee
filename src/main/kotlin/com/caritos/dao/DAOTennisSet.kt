package com.caritos.dao

import com.caritos.models.*

interface DAOTennisSet {
    suspend fun getAll(): List<TennisSet>
    suspend fun get(id: Int): TennisSet?
    suspend fun add(matchId: Int, setNumber: Int, teamAId: Int, teamBId: Int, teamAScore: Int, teamBScore: Int): TennisSet?
    suspend fun edit(id: Int, matchId: Int, setNumber: Int, teamAId: Int, teamBId: Int, teamAScore: Int, teamBScore: Int): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun getAllForMatch(id: Int): List<TennisSet>
    suspend fun getTennisSetsForMatch(id: Int): List<TennisSet>
    suspend fun getTeamsWithScores(): List<Pair<Int, Int>> 
}