package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.ToolVerseDao
import com.example.data.local.entities.AdminCustomToolEntity
import com.example.data.local.entities.MovieReviewEntity
import com.example.data.local.entities.SavedToolEntity
import com.example.data.local.entities.UserProjectEntity

@Database(
    entities = [
        UserProjectEntity::class,
        SavedToolEntity::class,
        MovieReviewEntity::class,
        AdminCustomToolEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toolVerseDao(): ToolVerseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "toolverse_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
