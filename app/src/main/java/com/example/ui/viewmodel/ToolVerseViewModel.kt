package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ai.GeminiAiService
import com.example.data.local.AppDatabase
import com.example.data.local.entities.AdminCustomToolEntity
import com.example.data.local.entities.MovieReviewEntity
import com.example.data.local.entities.UserProjectEntity
import com.example.data.model.AiChatMessage
import com.example.data.model.MovieItem
import com.example.data.model.SectionType
import com.example.data.model.SocialPostDraft
import com.example.data.model.ToolCategory
import com.example.data.model.ToolDefinition
import com.example.data.model.UserProfile
import com.example.data.model.WebsiteProject
import com.example.data.model.WebsiteSection
import com.example.data.repository.ToolVerseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.UUID

class ToolVerseViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = ToolVerseRepository(db.toolVerseDao())
    val aiService = GeminiAiService()

    // Global Search & Categories
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<ToolCategory?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    // UI Theme state
    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode = _isDarkMode.asStateFlow()

    // Notification toast
    private val _notificationMessage = MutableStateFlow<String?>(null)
    val notificationMessage = _notificationMessage.asStateFlow()

    // User Profile & Authentication State
    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile = _userProfile.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(true)
    val isAuthenticated = _isAuthenticated.asStateFlow()

    // AI Assistant Chat Messages
    private val _aiChatMessages = MutableStateFlow<List<AiChatMessage>>(
        listOf(
            AiChatMessage(
                id = "1",
                sender = "verse_ai",
                message = "Hello! I am Verse AI, your creative digital copilot. Ask me to generate a website, write social captions, explain code, create video scripts, or guide you through any of our 35+ tools!"
            )
        )
    )
    val aiChatMessages = _aiChatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking = _isAiThinking.asStateFlow()

    // Website Builder State
    private val _websiteProject = MutableStateFlow(
        WebsiteProject(
            id = UUID.randomUUID().toString(),
            siteName = "Artisan Cafe & Bistro",
            prompt = "Create a restaurant website with menu, gallery, contact page and online booking.",
            themeColorHex = "#00E5FF",
            sections = aiService.generateWebsiteSectionsFromPrompt("restaurant")
        )
    )
    val websiteProject = _websiteProject.asStateFlow()

    // Coding Tools State
    private val _codeLanguage = MutableStateFlow("HTML / CSS / JS")
    val codeLanguage = _codeLanguage.asStateFlow()

    private val _codeContent = MutableStateFlow(
        """<!DOCTYPE html>
<html>
<head>
  <style>
    body { font-family: sans-serif; background: #0b0f19; color: #f8fafc; display: flex; flex-direction: column; align-items: center; justify-content: center; height: 100vh; margin: 0; }
    .card { background: #121826; border: 1px solid #00E5FF; padding: 24px; border-radius: 16px; text-align: center; box-shadow: 0 8px 32px rgba(0,229,255,0.2); }
    h1 { color: #00E5FF; margin-bottom: 8px; }
    button { background: linear-gradient(135deg, #00E5FF, #7B2CBF); color: white; border: none; padding: 12px 24px; border-radius: 8px; font-weight: bold; cursor: pointer; }
  </style>
</head>
<body>
  <div class="card">
    <h1>Hello from ToolVerse AI!</h1>
    <p>Live frontend sandbox playground running seamlessly.</p>
    <button onclick="alert('ToolVerse AI sandbox interactive!')">Click Me</button>
  </div>
</body>
</html>"""
    )
    val codeContent = _codeContent.asStateFlow()

    private val _jsonInput = MutableStateFlow("{\n  \"platform\": \"ToolVerse AI\",\n  \"version\": \"1.0.0\",\n  \"toolsCount\": 35,\n  \"status\": \"operational\"\n}")
    val jsonInput = _jsonInput.asStateFlow()
    val jsonValidationResult = MutableStateFlow("Valid JSON (4 keys parsed)")

    private val _regexPattern = MutableStateFlow("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    val regexPattern = _regexPattern.asStateFlow()

    private val _regexTestText = MutableStateFlow("Contact support@toolverse.ai or alex.dev@startup.io for any enterprise questions.")
    val regexTestText = _regexTestText.asStateFlow()

    private val _markdownContent = MutableStateFlow(
        "# ToolVerse AI Documentation\n\nWelcome to **ToolVerse AI**, the all-in-one digital productivity ecosystem.\n\n### Key Modules:\n- 🎨 **Photo Tools**: AI removal, resize, filter studio\n- 🎬 **Video Tools**: Timeline editor, compressor, subtitles\n- 💻 **Coding Tools**: Multi-language IDE, JSON, Regex, Markdown\n- 🌐 **Website Builder**: No-code prompt-to-site generator\n\n```kotlin\nval tool = ToolVerse.create()\ntool.launch()\n```"
    )
    val markdownContent = _markdownContent.asStateFlow()

    // Social Media State
    private val _socialPostDraft = MutableStateFlow(
        SocialPostDraft(
            id = "draft_1",
            content = "🚀 Excited to unveil ToolVerse AI: the complete digital tools platform powering creators and engineers everywhere! Try the new suite today.",
            platforms = listOf("X (Twitter)", "LinkedIn", "Instagram"),
            scheduledTime = "Tomorrow at 10:00 AM UTC",
            hashtags = listOf("#ToolVerseAI", "#Productivity", "#SaaS", "#CreativeTools", "#Tech")
        )
    )
    val socialPostDraft = _socialPostDraft.asStateFlow()

    // Room Database Observables
    val userProjects: StateFlow<List<UserProjectEntity>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedToolsList: StateFlow<List<com.example.data.local.entities.SavedToolEntity>> = repository.savedTools
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val movieReviews: StateFlow<List<MovieReviewEntity>> = repository.movieReviews
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val adminCustomTools: StateFlow<List<AdminCustomToolEntity>> = repository.adminCustomTools
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTools: StateFlow<List<ToolDefinition>> = adminCustomTools.combine(selectedCategory) { custom, cat ->
        val mappedCustom = custom.map { c ->
            val toolCat = ToolCategory.entries.find { it.id == c.categoryId } ?: ToolCategory.CODING_TOOLS
            ToolDefinition(
                id = c.id,
                name = c.name,
                category = toolCat,
                description = c.description,
                isPro = c.isPro,
                isFeatured = c.isFeatured,
                iconName = c.iconName,
                tags = c.tags.split(",").map { it.trim() }
            )
        }
        repository.builtInTools + mappedCustom
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.builtInTools)

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSelectedCategory(category: ToolCategory?) {
        _selectedCategory.value = category
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun showNotification(message: String) {
        _notificationMessage.value = message
    }

    fun clearNotification() {
        _notificationMessage.value = null
    }

    fun toggleFavorite(toolId: String, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.toggleFavoriteTool(toolId, currentStatus)
            showNotification(if (!currentStatus) "Added to Favorites" else "Removed from Favorites")
        }
    }

    fun recordToolUsage(toolId: String) {
        viewModelScope.launch {
            repository.recordToolUsage(toolId)
        }
    }

    // AI Chat interaction
    fun sendVerseChatMessage(text: String) {
        if (text.isBlank()) return
        val userMsg = AiChatMessage(
            id = UUID.randomUUID().toString(),
            sender = "user",
            message = text
        )
        _aiChatMessages.value = _aiChatMessages.value + userMsg
        _isAiThinking.value = true

        viewModelScope.launch {
            val response = aiService.processVerseChat(text)
            _aiChatMessages.value = _aiChatMessages.value + response
            _isAiThinking.value = false

            // Deduct AI credits
            val current = _userProfile.value
            _userProfile.value = current.copy(
                aiCreditsUsed = (current.aiCreditsUsed + 1).coerceAtMost(current.aiCreditsLimit)
            )
        }
    }

    // Website Builder Actions
    fun generateWebsiteFromPrompt(prompt: String) {
        _isAiThinking.value = true
        viewModelScope.launch {
            val sections = aiService.generateWebsiteSectionsFromPrompt(prompt)
            _websiteProject.value = _websiteProject.value.copy(
                prompt = prompt,
                siteName = prompt.take(30).replaceFirstChar { it.uppercase() },
                sections = sections
            )
            _isAiThinking.value = false
            showNotification("AI Generated New Website Layout!")
        }
    }

    fun updateWebsiteThemeColor(hexColor: String) {
        _websiteProject.value = _websiteProject.value.copy(themeColorHex = hexColor)
    }

    fun updateWebsiteFontStyle(fontStyle: String) {
        _websiteProject.value = _websiteProject.value.copy(fontStyle = fontStyle)
    }

    fun updateWebsiteSection(sectionId: String, updatedSection: WebsiteSection) {
        val updatedList = _websiteProject.value.sections.map {
            if (it.id == sectionId) updatedSection else it
        }
        _websiteProject.value = _websiteProject.value.copy(sections = updatedList)
    }

    fun removeWebsiteSection(sectionId: String) {
        val updatedList = _websiteProject.value.sections.filter { it.id != sectionId }
        _websiteProject.value = _websiteProject.value.copy(sections = updatedList)
    }

    fun addWebsiteSection(type: SectionType) {
        val newSection = WebsiteSection(
            id = UUID.randomUUID().toString(),
            type = type,
            title = "New ${type.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }} Section",
            subtitle = "Customizable block content",
            content = "Click to modify details and styling for this block."
        )
        _websiteProject.value = _websiteProject.value.copy(sections = _websiteProject.value.sections + newSection)
    }

    fun togglePublishWebsite() {
        val current = _websiteProject.value.isPublished
        _websiteProject.value = _websiteProject.value.copy(isPublished = !current)
        showNotification(if (!current) "Website Published to Live Domain!" else "Website Unpublished")
    }

    fun saveWebsiteProject() {
        viewModelScope.launch {
            val project = _websiteProject.value
            val dataJson = JSONObject().apply {
                put("siteName", project.siteName)
                put("themeColorHex", project.themeColorHex)
                put("fontStyle", project.fontStyle)
                put("isPublished", project.isPublished)
                put("sectionsCount", project.sections.size)
            }.toString()

            repository.saveProject(
                id = project.id,
                title = project.siteName,
                toolId = "website_builder",
                categoryId = ToolCategory.WEBSITE_BUILDER.id,
                dataJson = dataJson,
                previewThumbnail = project.sections.firstOrNull()?.imageUrl
            )
            showNotification("Website Project Saved to Dashboard!")
        }
    }

    // Coding Tools Actions
    fun updateCodeContent(code: String) {
        _codeContent.value = code
    }

    fun updateCodeLanguage(lang: String) {
        _codeLanguage.value = lang
    }

    fun updateJsonInput(input: String) {
        _jsonInput.value = input
        try {
            JSONObject(input)
            jsonValidationResult.value = "Valid JSON (Object format verified)"
        } catch (e: Exception) {
            try {
                org.json.JSONArray(input)
                jsonValidationResult.value = "Valid JSON (Array format verified)"
            } catch (e2: Exception) {
                jsonValidationResult.value = "Invalid JSON: ${e.message?.take(40)}"
            }
        }
    }

    fun formatJson() {
        try {
            val trimmed = _jsonInput.value.trim()
            if (trimmed.startsWith("{")) {
                val obj = JSONObject(trimmed)
                _jsonInput.value = obj.toString(2)
                jsonValidationResult.value = "Formatted JSON successfully"
            } else if (trimmed.startsWith("[")) {
                val arr = org.json.JSONArray(trimmed)
                _jsonInput.value = arr.toString(2)
                jsonValidationResult.value = "Formatted JSON array successfully"
            }
        } catch (e: Exception) {
            jsonValidationResult.value = "Formatting error: ${e.message}"
        }
    }

    fun minifyJson() {
        try {
            val trimmed = _jsonInput.value.trim()
            if (trimmed.startsWith("{")) {
                val obj = JSONObject(trimmed)
                _jsonInput.value = obj.toString()
                jsonValidationResult.value = "Minified JSON successfully"
            } else if (trimmed.startsWith("[")) {
                val arr = org.json.JSONArray(trimmed)
                _jsonInput.value = arr.toString()
                jsonValidationResult.value = "Minified JSON array successfully"
            }
        } catch (e: Exception) {
            jsonValidationResult.value = "Minifying error: ${e.message}"
        }
    }

    fun updateRegex(pattern: String, testText: String) {
        _regexPattern.value = pattern
        _regexTestText.value = testText
    }

    fun updateMarkdown(content: String) {
        _markdownContent.value = content
    }

    // Social Media Actions
    fun updateSocialDraft(content: String, platforms: List<String>, hashtags: List<String>) {
        _socialPostDraft.value = _socialPostDraft.value.copy(
            content = content,
            platforms = platforms,
            hashtags = hashtags
        )
    }

    fun generateAiCaption(topic: String, tone: String) {
        _isAiThinking.value = true
        viewModelScope.launch {
            val prompt = "Write a high-converting social media post caption about '$topic' with a '$tone' tone. Include call to actions."
            val caption = aiService.generateWithGemini(prompt)
            _socialPostDraft.value = _socialPostDraft.value.copy(
                content = caption
            )
            _isAiThinking.value = false
            showNotification("AI Caption Generated!")
        }
    }

    // Movie Discovery Actions
    fun toggleMovieWatchlist(movie: MovieItem) {
        viewModelScope.launch {
            val existing = movieReviews.value.find { it.movieId == movie.id }
            val newStatus = !(existing?.isWatchlist ?: false)
            repository.saveMovieReview(
                movieId = movie.id,
                title = movie.title,
                year = movie.year,
                posterUrl = movie.posterUrl,
                isWatchlist = newStatus,
                userRating = existing?.userRating ?: 0,
                userReview = existing?.userReview ?: ""
            )
            showNotification(if (newStatus) "Added to Watchlist" else "Removed from Watchlist")
        }
    }

    fun saveMovieReview(movieId: String, title: String, year: String, posterUrl: String, rating: Int, review: String) {
        viewModelScope.launch {
            val existing = movieReviews.value.find { it.movieId == movieId }
            repository.saveMovieReview(
                movieId = movieId,
                title = title,
                year = year,
                posterUrl = posterUrl,
                isWatchlist = existing?.isWatchlist ?: false,
                userRating = rating,
                userReview = review
            )
            showNotification("Movie review and rating saved!")
        }
    }

    // Admin Actions
    fun addAdminCustomTool(name: String, categoryId: String, description: String, isPro: Boolean, isFeatured: Boolean, tags: String) {
        viewModelScope.launch {
            val entity = AdminCustomToolEntity(
                id = "custom_" + UUID.randomUUID().toString().take(8),
                name = name,
                categoryId = categoryId,
                description = description,
                isPro = isPro,
                isFeatured = isFeatured,
                tags = tags
            )
            repository.addCustomTool(entity)
            showNotification("Custom Tool '$name' published to catalog!")
        }
    }

    fun deleteAdminTool(id: String) {
        viewModelScope.launch {
            repository.deleteCustomTool(id)
            showNotification("Tool removed from catalog")
        }
    }

    // User Profile Plan Upgrade
    fun upgradeToProPlan() {
        _userProfile.value = _userProfile.value.copy(
            planType = "Pro",
            aiCreditsLimit = 500,
            storageLimitMb = 5000.0
        )
        showNotification("Upgraded to ToolVerse PRO Tier!")
    }

    fun downgradeToFreePlan() {
        _userProfile.value = _userProfile.value.copy(
            planType = "Free",
            aiCreditsLimit = 50,
            storageLimitMb = 500.0
        )
        showNotification("Switched to Free Tier")
    }

    fun deleteUserProject(id: String) {
        viewModelScope.launch {
            repository.deleteProject(id)
            showNotification("Project deleted")
        }
    }
}
