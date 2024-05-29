package com.caritos.models

import org.jetbrains.exposed.dao.id.IntIdTable

data class Court(val id: Int, val name: String, val location: String)

object Courts : IntIdTable() {
    val name = varchar("name", 100).uniqueIndex()
    val location = varchar("location", 255)
}