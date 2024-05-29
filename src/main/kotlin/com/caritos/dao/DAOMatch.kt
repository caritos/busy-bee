package com.caritos.dao

import com.caritos.models.*
import java.time.LocalDateTime

interface DAOMatch {
    suspend fun getAll(): List<Match>
    suspend fun get(id: Int): Match?
    suspend fun add(date: LocalDateTime, courtId: Int, winnerId: Int, loserId: Int, isDoubles: Boolean): Match?
    suspend fun edit(id: Int, date: LocalDateTime, courtId: Int, winnerId: Int, loserId: Int, isDoubles: Boolean): Boolean
    suspend fun delete(id: Int): Boolean
}