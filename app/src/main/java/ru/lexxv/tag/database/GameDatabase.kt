package ru.lexxv.tag.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.lexxv.tag.database.dao.GameStatsDao
import ru.lexxv.tag.database.table.GameStats

@Database(entities = [GameStats::class], version = 1)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameStatsDao(): GameStatsDao

    companion object {
        @Volatile
        private var INSTANCE: GameDatabase? = null

        fun getDatabase(context: Context): GameDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    "game_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}