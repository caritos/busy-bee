package com.caritos.dao

import com.caritos.models.Court

interface DAOCourt {
    suspend fun getAllCourts(): List<Court>
    suspend fun court(id: Int): Court?
    suspend fun addCourt(name: String, location: String): Court?
    suspend fun editCourt(id: Int, name: String, location: String): Boolean
    suspend fun deleteCourt(id: Int): Boolean
}