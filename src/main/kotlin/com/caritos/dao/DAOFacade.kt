package com.caritos.dao

import com.caritos.models.*
import java.time.LocalDateTime

interface DAOFacade {
    suspend fun allArticles(): List<Article>
    suspend fun article(id: Int): Article?
    suspend fun addNewArticle(title: String, body: String): Article?
    suspend fun editArticle(id: Int, title: String, body: String): Boolean
    suspend fun deleteArticle(id: Int): Boolean
    suspend fun addCourt(name: String, location: String): Int
    suspend fun addMatch(date: LocalDateTime, courtId: Int, winnerId: Int, loserId: Int, isDoubles: Boolean): Int
    suspend fun addTeam(matchId: Int, playerId: Int): Int
    suspend fun addSetScore(matchId: Int, setNumber: Int, player1Score: Int, player2Score: Int): Int
    suspend fun addPlayer(name: String): Int
    suspend fun getAllCourts(): List<Court>
    suspend fun getAllPlayers(): List<Player>
    suspend fun getAllMatches(): List<Match>
    suspend fun getAllTeams(): List<Team>
    suspend fun getAllSets(): List<TennisSet>
    suspend fun getRankings(): List<Pair<String, Int>>


}