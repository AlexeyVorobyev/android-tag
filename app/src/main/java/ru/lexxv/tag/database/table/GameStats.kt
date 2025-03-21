package ru.lexxv.tag.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_stats")
data class GameStats(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val moves: Int,
    val time: Float,
    val userName: String
)
