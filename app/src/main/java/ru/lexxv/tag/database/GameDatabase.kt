package ru.lexxv.tag.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.lexxv.tag.database.dao.GameStatsDao
import ru.lexxv.tag.database.table.GameStats

@Database(entities = [GameStats::class], version = 2)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameStatsDao(): GameStatsDao

    companion object {
        @Volatile
        private var INSTANCE: GameDatabase? = null

        private var MIGRATIONS: Array<Migration> = listOf(
            Migration(
                1,
                2
            ) {
                it.execSQL("ALTER TABLE game_stats ADD COLUMN userName TEXT NOT NULL DEFAULT ''")
            }
        ).toTypedArray()

        fun getDatabase(context: Context): GameDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameDatabase::class.java,
                    "game_database"
                ).addMigrations(*MIGRATIONS).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
