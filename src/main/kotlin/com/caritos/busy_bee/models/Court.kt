package com.caritos.busy_bee.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class Court(
    val id: Int,
    val name: String,
    val location: String
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

object CourtTable: IntIdTable("courts") {
    val name = varchar("name", 100).uniqueIndex()
    val location = varchar("location", 255)
}


class CourtDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CourtDAO>(CourtTable)
    var name by CourtTable.name
    var location by CourtTable.location
}

fun daoToModel(dao: CourtDAO) = Court(
    id = dao.id.value,
    name = dao.name,
    location = dao.location
)