package com.example.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.data.local.entities.MovieReviewEntity
import com.example.data.model.MovieItem
import com.example.data.model.SectionType
import com.example.data.model.SocialPostDraft
import com.example.data.model.ToolCategory
import com.example.data.model.ToolDefinition
import com.example.data.model.WebsiteProject
import com.example.data.model.WebsiteSection

@Composable
fun ToolWorkspaceScreen(
    toolId: String,
    allTools: List<ToolDefinition>,
    // Website builder
    websiteProject: WebsiteProject,
    isAiThinking: Boolean,
    onGenerateWebsite: (String) -> Unit,
    onUpdateThemeColor: (String) -> Unit,
    onUpdateFontStyle: (String) -> Unit,
    onUpdateSection: (String, WebsiteSection) -> Unit,
    onRemoveSection: (String) -> Unit,
    onAddSection: (SectionType) -> Unit,
    onTogglePublish: () -> Unit,
    onSaveProject: () -> Unit,
    // Coding tools
    codeContent: String,
    onCodeContentChange: (String) -> Unit,
    jsonInput: String,
    onJsonInputChange: (String) -> Unit,
    jsonValidation: String,
    onFormatJson: () -> Unit,
    onMinifyJson: () -> Unit,
    regexPattern: String,
    regexTestText: String,
    onUpdateRegex: (String, String) -> Unit,
    markdownContent: String,
    onMarkdownContentChange: (String) -> Unit,
    onAskAiToExplainCode: (String, String) -> Unit,
    // Social media
    postDraft: SocialPostDraft,
    onUpdateDraft: (String, List<String>, List<String>) -> Unit,
    onGenerateAiCaption: (String, String) -> Unit,
    // Movie & Ent
    movies: List<MovieItem>,
    movieReviews: List<MovieReviewEntity>,
    onToggleWatchlist: (MovieItem) -> Unit,
    onSaveMovieReview: (String, String, String, String, Int, String) -> Unit,
    // Notifications & navigation
    onShowNotification: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tool = allTools.find { it.id == toolId }
    val category = tool?.category ?: ToolCategory.WEBSITE_BUILDER

    when {
        toolId == "website_builder" || category == ToolCategory.WEBSITE_BUILDER -> {
            WebsiteBuilderScreen(
                websiteProject = websiteProject,
                isAiThinking = isAiThinking,
                onGenerateWebsite = onGenerateWebsite,
                onUpdateThemeColor = onUpdateThemeColor,
                onUpdateFontStyle = onUpdateFontStyle,
                onUpdateSection = onUpdateSection,
                onRemoveSection = onRemoveSection,
                onAddSection = onAddSection,
                onTogglePublish = onTogglePublish,
                onSaveProject = onSaveProject,
                onBack = onBack,
                modifier = modifier
            )
        }

        category == ToolCategory.CODING_TOOLS -> {
            CodePlaygroundScreen(
                initialToolId = toolId,
                codeContent = codeContent,
                onCodeContentChange = onCodeContentChange,
                jsonInput = jsonInput,
                onJsonInputChange = onJsonInputChange,
                jsonValidation = jsonValidation,
                onFormatJson = onFormatJson,
                onMinifyJson = onMinifyJson,
                regexPattern = regexPattern,
                regexTestText = regexTestText,
                onUpdateRegex = onUpdateRegex,
                markdownContent = markdownContent,
                onMarkdownContentChange = onMarkdownContentChange,
                isAiThinking = isAiThinking,
                onAskAiToExplainCode = onAskAiToExplainCode,
                onBack = onBack,
                modifier = modifier
            )
        }

        category == ToolCategory.PHOTO_TOOLS -> {
            PhotoToolsScreen(
                initialToolId = toolId,
                onShowNotification = onShowNotification,
                onBack = onBack,
                modifier = modifier
            )
        }

        category == ToolCategory.VIDEO_TOOLS -> {
            VideoToolsScreen(
                initialToolId = toolId,
                onShowNotification = onShowNotification,
                onBack = onBack,
                modifier = modifier
            )
        }

        category == ToolCategory.MOVIE_ENT -> {
            MovieDiscoveryScreen(
                initialToolId = toolId,
                movies = movies,
                movieReviews = movieReviews,
                onToggleWatchlist = onToggleWatchlist,
                onSaveReview = onSaveMovieReview,
                onShowNotification = onShowNotification,
                onBack = onBack,
                modifier = modifier
            )
        }

        category == ToolCategory.SOCIAL_MEDIA -> {
            SocialMediaScreen(
                initialToolId = toolId,
                postDraft = postDraft,
                onUpdateDraft = onUpdateDraft,
                isAiThinking = isAiThinking,
                onGenerateAiCaption = onGenerateAiCaption,
                onShowNotification = onShowNotification,
                onBack = onBack,
                modifier = modifier
            )
        }

        else -> {
            WebsiteBuilderScreen(
                websiteProject = websiteProject,
                isAiThinking = isAiThinking,
                onGenerateWebsite = onGenerateWebsite,
                onUpdateThemeColor = onUpdateThemeColor,
                onUpdateFontStyle = onUpdateFontStyle,
                onUpdateSection = onUpdateSection,
                onRemoveSection = onRemoveSection,
                onAddSection = onAddSection,
                onTogglePublish = onTogglePublish,
                onSaveProject = onSaveProject,
                onBack = onBack,
                modifier = modifier
            )
        }
    }
}
