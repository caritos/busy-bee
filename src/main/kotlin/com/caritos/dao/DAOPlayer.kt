package com.caritos.dao

import com.caritos.models.*

interface DAOPlayer {
    suspend fun getAll(): List<Player>
    suspend fun get(id: Int): Player?
    suspend fun add(name: String): Player?
    suspend fun edit(id: Int, name: String): Boolean
    suspend fun delete(id: Int): Boolean
}