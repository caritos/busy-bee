package com.caritos.dao

import com.caritos.models.*
import java.time.LocalDateTime

interface DAOMatch {
    suspend fun getAll(): List<Match>
    suspend fun get(id: Int): Match?
    suspend fun add(date: LocalDateTime, courtId: Int, teamAId: Int, teamBId: Int): Match?
    suspend fun edit(id: Int, date: LocalDateTime, courtId: Int, teamAId: Int, teamBId: Int): Boolean
    suspend fun delete(id: Int): Boolean
}