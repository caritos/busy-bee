package com.caritos.models

import kotlinx.serialization.Serializable

@Serializable
data class Court(
    val id: Int,
    val name: String,
    val location: String
)

@Serializable
data class Player(
    val id: Int,
    val name: String
)

interface CourtRepository {
    suspend fun allCourts(): List<Court>
    suspend fun courtById(id: Int): Court?
    suspend fun courtByName(name: String): Court?
    suspend fun courtsByLocation(name: String): List<Court>
    suspend fun addCourt(name: String, location: String): Court?
    suspend fun updateCourt(id: Int, name: String, location: String) 
    suspend fun removeCourt(id: Int): Boolean
}

interface PlayerRepository {
    suspend fun allPlayers(): List<Player>
    suspend fun playerById(id: Int): Player?
    suspend fun playerByName(name: String): Player?
    suspend fun addPlayer(name: String): Player?
    suspend fun updatePlayer(id: Int, name: String)
    suspend fun removePlayer(id: Int): Boolean
}