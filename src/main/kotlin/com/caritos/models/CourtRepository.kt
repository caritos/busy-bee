package com.caritos.models

interface CourtRepository {
    suspend fun allCourts(): List<Court>
    suspend fun courtById(id: Int): Court?
    suspend fun courtByName(name: String): Court?
    suspend fun courtsByLocation(name: String): List<Court>
    suspend fun addCourt(name: String, location: String): Court?
    suspend fun updateCourt(id: Int, name: String, location: String) 
    suspend fun removeCourt(id: Int): Boolean
}