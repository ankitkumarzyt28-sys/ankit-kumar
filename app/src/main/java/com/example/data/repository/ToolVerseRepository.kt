package com.example.data.repository

import com.example.data.local.dao.ToolVerseDao
import com.example.data.local.entities.AdminCustomToolEntity
import com.example.data.local.entities.MovieReviewEntity
import com.example.data.local.entities.SavedToolEntity
import com.example.data.local.entities.UserProjectEntity
import com.example.data.model.MovieItem
import com.example.data.model.ToolCategory
import com.example.data.model.ToolDefinition
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.UUID

class ToolVerseRepository(private val dao: ToolVerseDao) {

    val builtInTools: List<ToolDefinition> = listOf(
        // 1. Social Media
        ToolDefinition(
            id = "social_manager",
            name = "Social Media Manager",
            category = ToolCategory.SOCIAL_MEDIA,
            description = "Centralized multi-channel publishing hub for X, Instagram, LinkedIn and YouTube.",
            isFeatured = true,
            isPopular = true,
            iconName = "Share",
            tags = listOf("social", "manager", "posts", "x", "instagram", "linkedin")
        ),
        ToolDefinition(
            id = "post_scheduler",
            name = "Post Scheduler",
            category = ToolCategory.SOCIAL_MEDIA,
            description = "Queue and automate social content releases across global time zones.",
            isPro = true,
            iconName = "CalendarToday",
            tags = listOf("social", "schedule", "calendar", "automation")
        ),
        ToolDefinition(
            id = "caption_gen",
            name = "Caption Generator",
            category = ToolCategory.SOCIAL_MEDIA,
            description = "AI-powered viral hook & caption generator tailored for target audience tones.",
            isFeatured = true,
            iconName = "AutoAwesome",
            tags = listOf("social", "ai", "captions", "copywriting")
        ),
        ToolDefinition(
            id = "hashtag_gen",
            name = "Hashtag Generator",
            category = ToolCategory.SOCIAL_MEDIA,
            description = "Discover high-volume, low-competition tags for maximal algorithmic reach.",
            iconName = "Tag",
            tags = listOf("social", "hashtags", "tags", "reach", "growth")
        ),
        ToolDefinition(
            id = "social_img_creator",
            name = "Social Media Image Creator",
            category = ToolCategory.SOCIAL_MEDIA,
            description = "Design high-converting carousel cards, quote graphics, and story banners.",
            isPopular = true,
            iconName = "Image",
            tags = listOf("social", "design", "canvas", "banners", "stories")
        ),
        ToolDefinition(
            id = "social_analytics",
            name = "Social Media Analytics",
            category = ToolCategory.SOCIAL_MEDIA,
            description = "Comprehensive engagement metrics, click-through rates, and audience breakdown.",
            isPro = true,
            iconName = "Insights",
            tags = listOf("social", "analytics", "stats", "metrics", "charts")
        ),

        // 2. Video Tools
        ToolDefinition(
            id = "video_editor",
            name = "Video Editor",
            category = ToolCategory.VIDEO_TOOLS,
            description = "Timeline-based video arranger, transition overlay, and audio mixer.",
            isFeatured = true,
            isPopular = true,
            iconName = "MovieEdit",
            tags = listOf("video", "editor", "timeline", "cut", "transition")
        ),
        ToolDefinition(
            id = "video_trimmer",
            name = "Video Trimmer",
            category = ToolCategory.VIDEO_TOOLS,
            description = "Frame-accurate video cutting tool with fast lossless export.",
            iconName = "ContentCut",
            tags = listOf("video", "trim", "cut", "lossless")
        ),
        ToolDefinition(
            id = "video_compressor",
            name = "Video Compressor",
            category = ToolCategory.VIDEO_TOOLS,
            description = "Shrink MP4/MOV file sizes by up to 85% while preserving 1080p visual fidelity.",
            isPopular = true,
            iconName = "Compress",
            tags = listOf("video", "compress", "size", "mp4", "reduce")
        ),
        ToolDefinition(
            id = "video_converter",
            name = "Video Converter",
            category = ToolCategory.VIDEO_TOOLS,
            description = "Convert between MP4, WebM, MKV, AVI, and GIF formats instantly.",
            iconName = "Transform",
            tags = listOf("video", "convert", "format", "mkv", "webm", "gif")
        ),
        ToolDefinition(
            id = "subtitle_gen",
            name = "Subtitle Generator",
            category = ToolCategory.VIDEO_TOOLS,
            description = "Auto-transcribe speech to SRT/VTT captions with synchronized word timestamps.",
            isPro = true,
            iconName = "ClosedCaption",
            tags = listOf("video", "subtitles", "srt", "captions", "transcription")
        ),
        ToolDefinition(
            id = "ai_video_gen",
            name = "AI Video Generator",
            category = ToolCategory.VIDEO_TOOLS,
            description = "Generate cinematic video scripts, scene storyboards, and Veo AI prompts.",
            isPro = true,
            isFeatured = true,
            iconName = "Videocam",
            tags = listOf("video", "ai", "veo", "cinematic", "storyboard")
        ),
        ToolDefinition(
            id = "thumbnail_maker",
            name = "Thumbnail Maker",
            category = ToolCategory.VIDEO_TOOLS,
            description = "Craft click-worthy 1280x720 YouTube & TikTok thumbnails with bold stickers and text.",
            isPopular = true,
            iconName = "PhotoSizeSelectActual",
            tags = listOf("video", "thumbnail", "youtube", "cover", "tiktok")
        ),
        ToolDefinition(
            id = "reels_shorts_maker",
            name = "Reel/Shorts Maker",
            category = ToolCategory.VIDEO_TOOLS,
            description = "Vertical 9:16 video creator with tempo guides, audio waveforms, and text presets.",
            isRecentlyAdded = true,
            iconName = "SmartDisplay",
            tags = listOf("video", "reels", "shorts", "tiktok", "vertical", "9:16")
        ),

        // 3. Photo Tools
        ToolDefinition(
            id = "photo_editor",
            name = "Photo Editor",
            category = ToolCategory.PHOTO_TOOLS,
            description = "Pro adjustment studio: contrast, saturation, exposure, vintage/cyberpunk LUT filters.",
            isFeatured = true,
            isPopular = true,
            iconName = "Tune",
            tags = listOf("photo", "editor", "filters", "lut", "retouch")
        ),
        ToolDefinition(
            id = "bg_remover",
            name = "Background Remover",
            category = ToolCategory.PHOTO_TOOLS,
            description = "One-tap AI subject isolation with transparent PNG alpha export and solid backdrops.",
            isFeatured = true,
            isPopular = true,
            iconName = "LayersClear",
            tags = listOf("photo", "background", "remove", "transparent", "png", "ai")
        ),
        ToolDefinition(
            id = "img_compressor",
            name = "Image Compressor",
            category = ToolCategory.PHOTO_TOOLS,
            description = "Smart JPEG/PNG lossy & lossless optimizer with live quality preview slider.",
            iconName = "Compress",
            tags = listOf("photo", "compress", "optimize", "jpeg", "png")
        ),
        ToolDefinition(
            id = "img_resizer",
            name = "Image Resizer",
            category = ToolCategory.PHOTO_TOOLS,
            description = "Preset & custom pixel resizing with aspect ratio lock and DPI preservation.",
            iconName = "AspectRatio",
            tags = listOf("photo", "resize", "scale", "dimensions", "crop")
        ),
        ToolDefinition(
            id = "img_converter",
            name = "Image Converter",
            category = ToolCategory.PHOTO_TOOLS,
            description = "Batch convert between PNG, JPG, WebP, SVG, BMP, and ICO formats.",
            iconName = "SwapHoriz",
            tags = listOf("photo", "convert", "webp", "svg", "ico")
        ),
        ToolDefinition(
            id = "ai_image_gen",
            name = "AI Image Generator",
            category = ToolCategory.PHOTO_TOOLS,
            description = "Synthesize photorealistic concept art, 3D renders, and illustrations from text.",
            isPro = true,
            isFeatured = true,
            iconName = "Brush",
            tags = listOf("photo", "ai", "art", "generate", "diffusion", "render")
        ),
        ToolDefinition(
            id = "logo_maker",
            name = "Logo Maker",
            category = ToolCategory.PHOTO_TOOLS,
            description = "Generate vector modern tech marks, monograms, and brand icon suites.",
            isRecentlyAdded = true,
            iconName = "Token",
            tags = listOf("photo", "logo", "branding", "vector", "monogram")
        ),
        ToolDefinition(
            id = "poster_maker",
            name = "Poster Maker",
            category = ToolCategory.PHOTO_TOOLS,
            description = "Design event flyers, concert posters, and promotional graphics with typography layers.",
            iconName = "DashboardCustomize",
            tags = listOf("photo", "poster", "flyer", "graphics", "print")
        ),

        // 4. Movie & Entertainment
        ToolDefinition(
            id = "movie_discovery",
            name = "Movie Discovery",
            category = ToolCategory.MOVIE_ENT,
            description = "Explore trending films, curated thematic collections, box office leaders and award winners.",
            isFeatured = true,
            isPopular = true,
            iconName = "Explore",
            tags = listOf("movie", "discovery", "trending", "cinema", "films")
        ),
        ToolDefinition(
            id = "movie_search",
            name = "Movie Search",
            category = ToolCategory.MOVIE_ENT,
            description = "Filter films across genres, release decades, cast members, and IMDb/TMDB scores.",
            iconName = "Search",
            tags = listOf("movie", "search", "filter", "cast", "genre")
        ),
        ToolDefinition(
            id = "movie_watchlist",
            name = "Watchlist Manager",
            category = ToolCategory.MOVIE_ENT,
            description = "Save upcoming releases and track your personal must-watch cinema backlog.",
            iconName = "Bookmark",
            tags = listOf("movie", "watchlist", "saved", "tracker")
        ),
        ToolDefinition(
            id = "movie_ratings",
            name = "Ratings & Scoreboard",
            category = ToolCategory.MOVIE_ENT,
            description = "View Rotten Tomatoes, Metacritic, and community verified score aggregates.",
            iconName = "Star",
            tags = listOf("movie", "ratings", "score", "metacritic", "tomatometer")
        ),
        ToolDefinition(
            id = "movie_reviews",
            name = "Movie Reviews & Journal",
            category = ToolCategory.MOVIE_ENT,
            description = "Write in-depth critique notes, star ratings, and community review journals.",
            iconName = "RateReview",
            tags = listOf("movie", "reviews", "journal", "critique", "notes")
        ),
        ToolDefinition(
            id = "trailer_search",
            name = "Official Trailer Search",
            category = ToolCategory.MOVIE_ENT,
            description = "Stream high-definition teasers, behind-the-scenes clips, and official trailers.",
            isRecentlyAdded = true,
            iconName = "OndemandVideo",
            tags = listOf("movie", "trailers", "youtube", "hd", "teasers")
        ),

        // 5. Coding Tools
        ToolDefinition(
            id = "code_editor",
            name = "Online Code Editor",
            category = ToolCategory.CODING_TOOLS,
            description = "Lightweight syntax-highlighted editor with multi-language code templates.",
            isFeatured = true,
            isPopular = true,
            iconName = "Terminal",
            tags = listOf("coding", "editor", "ide", "syntax", "code")
        ),
        ToolDefinition(
            id = "web_playground",
            name = "HTML/CSS/JS Playground",
            category = ToolCategory.CODING_TOOLS,
            description = "Interactive frontend sandbox with real-time responsive DOM rendering.",
            isFeatured = true,
            isPopular = true,
            iconName = "Html",
            tags = listOf("coding", "html", "css", "javascript", "playground", "sandbox")
        ),
        ToolDefinition(
            id = "json_formatter",
            name = "JSON Formatter & Validator",
            category = ToolCategory.CODING_TOOLS,
            description = "Pretty-print, minify, validate, and convert JSON schemas with error pointers.",
            isPopular = true,
            iconName = "DataArray",
            tags = listOf("coding", "json", "formatter", "validator", "minify")
        ),
        ToolDefinition(
            id = "code_formatter",
            name = "Universal Code Formatter",
            category = ToolCategory.CODING_TOOLS,
            description = "Auto-format Python, JavaScript, TypeScript, SQL, Kotlin, and Rust source files.",
            iconName = "FormatAlignLeft",
            tags = listOf("coding", "formatter", "prettier", "python", "typescript", "sql")
        ),
        ToolDefinition(
            id = "markdown_editor",
            name = "Markdown Editor",
            category = ToolCategory.CODING_TOOLS,
            description = "Split-view GitHub Flavored Markdown editor with live preview and PDF/HTML export.",
            iconName = "TextSnippet",
            tags = listOf("coding", "markdown", "gfm", "docs", "readme")
        ),
        ToolDefinition(
            id = "regex_tester",
            name = "Regex Tester & Debugger",
            category = ToolCategory.CODING_TOOLS,
            description = "Evaluate regular expressions against sample text with live capture group breakdown.",
            iconName = "Spellcheck",
            tags = listOf("coding", "regex", "regexp", "tester", "pattern", "matcher")
        ),
        ToolDefinition(
            id = "api_tester",
            name = "API Tester & Client",
            category = ToolCategory.CODING_TOOLS,
            description = "Send REST GET/POST/PUT/DELETE requests with custom headers, query params, and body payload.",
            isPro = true,
            iconName = "Http",
            tags = listOf("coding", "api", "rest", "postman", "http", "curl")
        ),
        ToolDefinition(
            id = "ai_coding_assistant",
            name = "AI Coding Assistant",
            category = ToolCategory.CODING_TOOLS,
            description = "Explain complex codebases, detect bugs, and refactor code with Verse AI.",
            isPro = true,
            isFeatured = true,
            iconName = "Psychology",
            tags = listOf("coding", "ai", "refactor", "debug", "assistant", "explain")
        ),

        // 6. Website Builder
        ToolDefinition(
            id = "website_builder",
            name = "No-Code AI Website Builder",
            category = ToolCategory.WEBSITE_BUILDER,
            description = "Prompt-to-Website generator with responsive preview, drag sections, live style customizer and export.",
            isFeatured = true,
            isPopular = true,
            isRecentlyAdded = true,
            iconName = "Web",
            tags = listOf("website", "builder", "no-code", "generator", "landing", "html")
        )
    )

    // Initial Movies List (Legal discovery metadata)
    val curatedMovies: List<MovieItem> = listOf(
        MovieItem(
            id = "m1",
            title = "Oppenheimer",
            year = "2023",
            rating = 8.9,
            genre = "Biography / Drama / History",
            overview = "The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb during World War II.",
            duration = "3h 00m",
            director = "Christopher Nolan",
            posterUrl = "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=600",
            trailerQuery = "Oppenheimer official trailer Universal Pictures"
        ),
        MovieItem(
            id = "m2",
            title = "Dune: Part Two",
            year = "2024",
            rating = 8.7,
            genre = "Action / Adventure / Sci-Fi",
            overview = "Paul Atreides unites with Chani and the Fremen while seeking revenge against the conspirators who destroyed his family.",
            duration = "2h 46m",
            director = "Denis Villeneuve",
            posterUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600",
            trailerQuery = "Dune Part Two official trailer Warner Bros"
        ),
        MovieItem(
            id = "m3",
            title = "Interstellar",
            year = "2014",
            rating = 8.7,
            genre = "Adventure / Drama / Sci-Fi",
            overview = "When Earth becomes uninhabitable in the future, a farmer and ex-NASA pilot is tasked to pilot a spacecraft along with a team of researchers to find a new planet for humans.",
            duration = "2h 49m",
            director = "Christopher Nolan",
            posterUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=600",
            trailerQuery = "Interstellar official trailer Paramount Pictures"
        ),
        MovieItem(
            id = "m4",
            title = "Spider-Man: Across the Spider-Verse",
            year = "2023",
            rating = 8.6,
            genre = "Animation / Action / Adventure",
            overview = "Miles Morales catapults across the Multiverse, where he encounters a team of Spider-People charged with protecting its very existence.",
            duration = "2h 20m",
            director = "Joaquim Dos Santos, Kemp Powers",
            posterUrl = "https://images.unsplash.com/photo-1635805737707-575885ab0820?w=600",
            trailerQuery = "Spider-Man Across the Spider-Verse Sony Pictures"
        ),
        MovieItem(
            id = "m5",
            title = "Inception",
            year = "2010",
            rating = 8.8,
            genre = "Action / Adventure / Sci-Fi",
            overview = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
            duration = "2h 28m",
            director = "Christopher Nolan",
            posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600",
            trailerQuery = "Inception trailer Warner Bros"
        ),
        MovieItem(
            id = "m6",
            title = "Blade Runner 2049",
            year = "2017",
            rating = 8.0,
            genre = "Action / Drama / Sci-Fi",
            overview = "Young Blade Runner K's discovery of a long-buried secret leads him to track down former Blade Runner Rick Deckard, who's been missing for thirty years.",
            duration = "2h 44m",
            director = "Denis Villeneuve",
            posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600",
            trailerQuery = "Blade Runner 2049 official trailer Warner Bros"
        )
    )

    // Flow queries
    val allProjects: Flow<List<UserProjectEntity>> = dao.getAllProjects()
    val savedTools: Flow<List<SavedToolEntity>> = dao.getAllSavedTools()
    val movieReviews: Flow<List<MovieReviewEntity>> = dao.getAllMovieReviews()
    val watchlist: Flow<List<MovieReviewEntity>> = dao.getWatchlist()
    val adminCustomTools: Flow<List<AdminCustomToolEntity>> = dao.getAllCustomTools()

    fun getToolById(id: String): ToolDefinition? {
        return builtInTools.find { it.id == id }
    }

    fun searchTools(query: String, selectedCategory: ToolCategory?): List<ToolDefinition> {
        val q = query.trim().lowercase()
        return builtInTools.filter { tool ->
            val matchesCategory = selectedCategory == null || tool.category == selectedCategory
            val matchesQuery = q.isEmpty() ||
                tool.name.lowercase().contains(q) ||
                tool.description.lowercase().contains(q) ||
                tool.category.title.lowercase().contains(q) ||
                tool.tags.any { it.lowercase().contains(q) }
            matchesCategory && matchesQuery
        }
    }

    suspend fun saveProject(
        id: String = UUID.randomUUID().toString(),
        title: String,
        toolId: String,
        categoryId: String,
        dataJson: String,
        previewThumbnail: String? = null
    ) {
        val project = UserProjectEntity(
            id = id,
            title = title,
            toolId = toolId,
            categoryId = categoryId,
            dataJson = dataJson,
            previewThumbnail = previewThumbnail,
            updatedAt = System.currentTimeMillis()
        )
        dao.insertProject(project)
    }

    suspend fun deleteProject(id: String) = dao.deleteProjectById(id)

    suspend fun toggleFavoriteTool(toolId: String, currentStatus: Boolean) {
        val existing = dao.getSavedTool(toolId)
        if (existing == null) {
            dao.insertSavedTool(SavedToolEntity(toolId = toolId, isFavorite = !currentStatus, usageCount = 1))
        } else {
            dao.updateFavoriteStatus(toolId, !currentStatus)
        }
    }

    suspend fun recordToolUsage(toolId: String) {
        val existing = dao.getSavedTool(toolId)
        if (existing == null) {
            dao.insertSavedTool(SavedToolEntity(toolId = toolId, usageCount = 1))
        } else {
            dao.incrementUsage(toolId)
        }
    }

    suspend fun saveMovieReview(
        movieId: String,
        title: String,
        year: String,
        posterUrl: String,
        isWatchlist: Boolean,
        userRating: Int,
        userReview: String
    ) {
        dao.saveMovieReview(
            MovieReviewEntity(
                movieId = movieId,
                title = title,
                year = year,
                posterUrl = posterUrl,
                isWatchlist = isWatchlist,
                userRating = userRating,
                userReview = userReview,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun toggleWatchlist(movie: MovieItem, isWatchlist: Boolean) {
        dao.saveMovieReview(
            MovieReviewEntity(
                movieId = movie.id,
                title = movie.title,
                year = movie.year,
                posterUrl = movie.posterUrl,
                isWatchlist = isWatchlist,
                userRating = movie.userRating,
                userReview = movie.userReview,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun addCustomTool(tool: AdminCustomToolEntity) {
        dao.insertCustomTool(tool)
    }

    suspend fun deleteCustomTool(id: String) {
        dao.deleteCustomTool(id)
    }
}
