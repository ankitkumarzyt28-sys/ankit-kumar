package com.example.data.model

enum class ToolCategory(val id: String, val title: String, val iconName: String, val description: String) {
    SOCIAL_MEDIA("social", "Social Media", "Share", "Manager, Schedulers, Captions, Hashtags & Analytics"),
    VIDEO_TOOLS("video", "Video Tools", "MovieCreation", "Editors, Trimmers, Compressors, Subtitles & AI Video"),
    PHOTO_TOOLS("photo", "Photo Tools", "PhotoFilter", "Photo Editor, BG Remover, Resizer & AI Image Gen"),
    MOVIE_ENT("movie", "Movie & Entertainment", "LiveTv", "Discovery, Ratings, Watchlists & Reviews"),
    CODING_TOOLS("coding", "Coding Tools", "Code", "Web Playground, Formatters, Regex & AI Assistant"),
    WEBSITE_BUILDER("website", "Website Builder", "Web", "AI No-Code Website Creator & Live Customizer")
}

data class ToolDefinition(
    val id: String,
    val name: String,
    val category: ToolCategory,
    val description: String,
    val isPro: Boolean = false,
    val isFeatured: Boolean = false,
    val isPopular: Boolean = false,
    val isRecentlyAdded: Boolean = false,
    val iconName: String,
    val tags: List<String> = emptyList(),
    val usageCount: Int = 0
)

data class WebsiteSection(
    val id: String,
    val type: SectionType,
    val title: String,
    val subtitle: String,
    val content: String,
    val buttonText: String = "Get Started",
    val imageUrl: String = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=600",
    val items: List<String> = emptyList(),
    val isVisible: Boolean = true
)

enum class SectionType {
    HERO, FEATURES, MENU_GALLERY, ABOUT, TESTIMONIALS, CONTACT_BOOKING, FOOTER
}

data class WebsiteProject(
    val id: String,
    val siteName: String,
    val prompt: String,
    val themeColorHex: String = "#00E5FF",
    val fontStyle: String = "Modern Sans",
    val sections: List<WebsiteSection> = emptyList(),
    val isPublished: Boolean = false,
    val customDomain: String = "toolverse.site/my-site"
)

data class MovieItem(
    val id: String,
    val title: String,
    val year: String,
    val rating: Double,
    val genre: String,
    val overview: String,
    val duration: String,
    val director: String,
    val posterUrl: String,
    val trailerQuery: String,
    val isWatchlist: Boolean = false,
    val userRating: Int = 0,
    val userReview: String = ""
)

data class SocialPostDraft(
    val id: String,
    val content: String,
    val platforms: List<String>,
    val scheduledTime: String,
    val mediaUri: String? = null,
    val hashtags: List<String> = emptyList(),
    val status: String = "Draft" // Draft, Scheduled, Published
)

data class AiChatMessage(
    val id: String,
    val sender: String, // "user" or "verse_ai"
    val message: String,
    val suggestedToolId: String? = null,
    val suggestedToolName: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class UserProfile(
    val name: String = "Alex Developer",
    val email: String = "alex.dev@toolverse.ai",
    val planType: String = "Free", // Free, Pro
    val aiCreditsUsed: Int = 18,
    val aiCreditsLimit: Int = 50,
    val storageUsedMb: Double = 142.5,
    val storageLimitMb: Double = 500.0,
    val isDarkMode: Boolean = true
)
