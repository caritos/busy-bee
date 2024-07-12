package com.caritos.db

import com.caritos.models.Court
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object CourtTable: IntIdTable("courts") {
    val name = varchar("name", 100).uniqueIndex()
    val location = varchar("location", 255)
}

class CourtDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CourtDAO>(CourtTable)
    var name by CourtTable.name
    var location by CourtTable.location
}

suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)

fun daoToModel(dao: CourtDAO) = Court(
    id = dao.id.value,
    name = dao.name,
    location = dao.location
)