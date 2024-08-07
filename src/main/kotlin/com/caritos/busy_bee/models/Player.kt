package com.caritos.busy_bee.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Player(
    val id: Int,
    val name: String
)

interface PlayerRepository {
    suspend fun allPlayers(): List<Player>
    suspend fun playerById(id: Int): Player?
    suspend fun playerByName(name: String): Player?
    suspend fun addPlayer(name: String): Player?
    suspend fun updatePlayer(id: Int, name: String)
    suspend fun removePlayer(id: Int): Boolean
}

object PlayerTable : IntIdTable("players") {
    val name = varchar("name", 50)
}

class PlayerDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PlayerDAO>(PlayerTable)
    var name by PlayerTable.name
}

fun daoToModel(dao: PlayerDAO) = Player(
    id = dao.id.value,
    name = dao.name,
)