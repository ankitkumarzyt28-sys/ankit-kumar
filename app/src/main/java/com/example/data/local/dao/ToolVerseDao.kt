package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.AdminCustomToolEntity
import com.example.data.local.entities.MovieReviewEntity
import com.example.data.local.entities.SavedToolEntity
import com.example.data.local.entities.UserProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ToolVerseDao {

    // User Projects
    @Query("SELECT * FROM user_projects ORDER BY updatedAt DESC")
    fun getAllProjects(): Flow<List<UserProjectEntity>>

    @Query("SELECT * FROM user_projects WHERE categoryId = :categoryId ORDER BY updatedAt DESC")
    fun getProjectsByCategory(categoryId: String): Flow<List<UserProjectEntity>>

    @Query("SELECT * FROM user_projects WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: String): UserProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: UserProjectEntity)

    @Query("DELETE FROM user_projects WHERE id = :id")
    suspend fun deleteProjectById(id: String)

    // Saved Tools & Favorites
    @Query("SELECT * FROM saved_tools")
    fun getAllSavedTools(): Flow<List<SavedToolEntity>>

    @Query("SELECT * FROM saved_tools WHERE toolId = :toolId LIMIT 1")
    suspend fun getSavedTool(toolId: String): SavedToolEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedTool(savedTool: SavedToolEntity)

    @Query("UPDATE saved_tools SET isFavorite = :isFavorite WHERE toolId = :toolId")
    suspend fun updateFavoriteStatus(toolId: String, isFavorite: Boolean)

    @Query("UPDATE saved_tools SET usageCount = usageCount + 1, lastUsedAt = :timestamp WHERE toolId = :toolId")
    suspend fun incrementUsage(toolId: String, timestamp: Long = System.currentTimeMillis())

    // Movie Watchlist & Reviews
    @Query("SELECT * FROM movie_reviews ORDER BY updatedAt DESC")
    fun getAllMovieReviews(): Flow<List<MovieReviewEntity>>

    @Query("SELECT * FROM movie_reviews WHERE isWatchlist = 1 ORDER BY updatedAt DESC")
    fun getWatchlist(): Flow<List<MovieReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMovieReview(review: MovieReviewEntity)

    @Query("DELETE FROM movie_reviews WHERE movieId = :movieId")
    suspend fun deleteMovieReview(movieId: String)

    // Admin Custom Tools
    @Query("SELECT * FROM admin_custom_tools")
    fun getAllCustomTools(): Flow<List<AdminCustomToolEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomTool(tool: AdminCustomToolEntity)

    @Query("DELETE FROM admin_custom_tools WHERE id = :id")
    suspend fun deleteCustomTool(id: String)
}
