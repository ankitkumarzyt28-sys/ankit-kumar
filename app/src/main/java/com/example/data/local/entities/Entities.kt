package com.example.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_projects")
data class UserProjectEntity(
    @PrimaryKey val id: String,
    val title: String,
    val toolId: String,
    val categoryId: String,
    val dataJson: String,
    val previewThumbnail: String? = null,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_tools")
data class SavedToolEntity(
    @PrimaryKey val toolId: String,
    val isFavorite: Boolean = false,
    val usageCount: Int = 0,
    val lastUsedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "movie_reviews")
data class MovieReviewEntity(
    @PrimaryKey val movieId: String,
    val title: String,
    val year: String,
    val posterUrl: String,
    val isWatchlist: Boolean = false,
    val userRating: Int = 0,
    val userReview: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "admin_custom_tools")
data class AdminCustomToolEntity(
    @PrimaryKey val id: String,
    val name: String,
    val categoryId: String,
    val description: String,
    val isPro: Boolean = false,
    val isFeatured: Boolean = false,
    val iconName: String = "Build",
    val tags: String = ""
)
