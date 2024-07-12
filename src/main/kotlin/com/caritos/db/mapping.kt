package com.caritos.db

import com.caritos.models.Court
import com.caritos.models.Player
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/**
 * Player
 */
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
/**
 * Court
 */
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


suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, statement = block)
