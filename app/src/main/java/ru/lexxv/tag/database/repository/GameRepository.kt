package ru.lexxv.tag.database.repository

import android.content.Context
import ru.lexxv.tag.database.GameDatabase
import ru.lexxv.tag.database.table.GameStats

class GameRepository(context: Context) {
    private val db = GameDatabase.getDatabase(context)
    private val gameStatsDao = db.gameStatsDao()

    suspend fun saveGameResult(moves: Int, time: Float, userName: String) {
        gameStatsDao.insertGameResult(GameStats(moves = moves, time = time, userName = userName))
    }

    suspend fun getAllGameResults(): List<GameStats> {
        return gameStatsDao.getAllGameResults()
    }

    suspend fun clearGameResults() {
        gameStatsDao.clearGameResults()
    }
}
