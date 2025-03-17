package ru.lexxv.tag.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.lexxv.tag.database.table.GameStats

@Dao
interface GameStatsDao {
    @Insert
    suspend fun insertGameResult(gameStats: GameStats)

    @Query("SELECT * FROM game_stats ORDER BY time ASC")
    suspend fun getAllGameResults(): List<GameStats>

    @Query("DELETE FROM game_stats")
    suspend fun clearGameResults()
}
