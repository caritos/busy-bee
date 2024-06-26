package com.caritos.dao

import com.caritos.models.*
import java.time.LocalDate

interface DAOMatch {
    suspend fun getAll(): List<Match>
    suspend fun get(id: Int): Match?
    suspend fun add(date: LocalDate, courtId: Int, teamAId: Int, teamBId: Int): Match?
    suspend fun edit(id: Int, date: LocalDate, courtId: Int, teamAId: Int, teamBId: Int): Boolean
    suspend fun delete(id: Int): Boolean
    suspend fun getRecentMatches(limit: Int): List<Match>
}