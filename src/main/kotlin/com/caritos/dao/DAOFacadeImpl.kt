package com.caritos.dao

import com.caritos.models.Article
import com.caritos.dao.DatabaseSingleton.dbQuery
import com.caritos.models.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime


class DAOFacadeImpl : DAOFacade {
    private fun resultRowToArticle(row: ResultRow) = Article(
        id = row[Articles.id],
        title = row[Articles.title],
        body = row[Articles.body],
    )

    override suspend fun allArticles(): List<Article> = dbQuery {
        Articles.selectAll().map(::resultRowToArticle)
    }

    override suspend fun article(id: Int): Article? = dbQuery {
        Articles
            .select { Articles.id eq id }
            .map(::resultRowToArticle)
            .singleOrNull()
    }

    override suspend fun addNewArticle(title: String, body: String): Article? = dbQuery {
        val insertStatement = Articles.insert {
            it[Articles.title] = title
            it[Articles.body] = body
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToArticle)
    }

    override suspend fun editArticle(id: Int, title: String, body: String): Boolean = dbQuery {
        Articles.update({ Articles.id eq id }) {
            it[Articles.title] = title
            it[Articles.body] = body
        } > 0
    }

    override suspend fun deleteArticle(id: Int): Boolean = dbQuery {
        Articles.deleteWhere { Articles.id eq id } > 0
    }



    override suspend fun addMatch(
        date: LocalDateTime,
        courtId: Int,
        winnerId: Int,
        loserId: Int,
        isDoubles: Boolean
    ): Int {
        return transaction {
            Matches.insertAndGetId {
                it[Matches.date] = date
                it[Matches.courtId] = courtId
                it[Matches.winnerId] = winnerId
                it[Matches.loserId] = loserId
                it[Matches.isDoubles] = isDoubles
            }.value
        }
    }

    override suspend fun addTeam(matchId: Int, playerId: Int): Int {
        return transaction {
            Teams.insertAndGetId {
                it[Teams.matchId] = matchId
                it[Teams.playerId] = playerId
            }.value
        }
    }

    override suspend fun addSetScore(matchId: Int, setNumber: Int, player1Score: Int, player2Score: Int): Int {
        return transaction {
            TennisSets.insertAndGetId {
                it[TennisSets.matchId] = matchId
                it[TennisSets.setNumber] = setNumber
                it[TennisSets.player1Score] = player1Score
                it[TennisSets.player2Score] = player2Score
            }.value
        }
    }

    override suspend fun addPlayer(name: String): Int {
        return transaction {
            Players.insertAndGetId {
                it[Players.name] = name
            }.value
        }
    }





    override suspend fun getAllPlayers(): List<Player> {
        return transaction {
            Players.selectAll().map {
                Player(
                    id = it[Players.id].value,
                    name = it[Players.name]
                )
            }
        }
    }

    override suspend fun getAllMatches(): List<Match> {
        return transaction {
            Matches.selectAll().map {
                Match(
                    id = it[Matches.id].value,
                    date = it[Matches.date],
                    courtId = it[Matches.courtId],
                    winnerId = it[Matches.winnerId],
                    loserId = it[Matches.loserId],
                    isDoubles = it[Matches.isDoubles]
                )
            }
        }
    }

    override suspend fun getAllTeams(): List<Team> {
        return transaction {
            Teams.selectAll().map {
                Team(
                    id = it[Teams.id].value,
                    playerId = it[Teams.playerId],
                    matchId = it[Teams.matchId]
                )
            }
        }
    }

    override suspend fun getAllSets(): List<TennisSet> {
        return transaction {
            TennisSets.selectAll().map {
                TennisSet(
                    id = it[TennisSets.id].value,
                    matchId = it[TennisSets.matchId],
                    setNumber = it[TennisSets.setNumber],
                    player1Score = it[TennisSets.player1Score],
                    player2Score = it[TennisSets.player2Score]
                )
            }
        }
    }

    override suspend fun getRankings(): List<Pair<String, Int>> {
        val playerPoints = calculatePlayerPoints()
        val playerRankings = mutableListOf<Pair<String, Int>>()

        transaction {
            playerPoints.forEach { (playerId, points) ->
                val playerName = Players.select { Players.id eq playerId }.single()[Players.name]
                playerRankings.add(playerName to points)
            }
        }

        return playerRankings.sortedByDescending { it.second }
    }

    fun calculatePlayerPoints(): Map<Int, Int> {
        val playerPoints = mutableMapOf<Int, Int>()

        transaction {
            // Calculate points for match wins
            Matches.selectAll().forEach { match ->
                val winnerId = match[Matches.winnerId]
                playerPoints[winnerId] = playerPoints.getOrDefault(winnerId, 0) + 10
            }

            // Calculate points for set wins
            TennisSets.selectAll().forEach { set ->
                val matchId = set[TennisSets.matchId]
                val match = Matches.select { Matches.id eq matchId }.single()
                val player1Score = set[TennisSets.player1Score]
                val player2Score = set[TennisSets.player2Score]

                val winnerId = if (player1Score > player2Score) match[Matches.winnerId] else match[Matches.loserId]
                playerPoints[winnerId] = playerPoints.getOrDefault(winnerId, 0) + 2
            }
        }

        return playerPoints
    }



}

val dao: DAOFacade = DAOFacadeImpl().apply {
    runBlocking {
        if(allArticles().isEmpty()) {
            addNewArticle("The drive to develop!", "...it's what keeps me going.")
        }
    }
}