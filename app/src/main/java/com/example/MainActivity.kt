package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.model.ToolCategory
import com.example.ui.components.NotificationBanner
import com.example.ui.components.ToolVerseTopBar
import com.example.ui.screens.AdminPanelScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ToolCatalogScreen
import com.example.ui.screens.ToolWorkspaceScreen
import com.example.ui.screens.UserDashboardScreen
import com.example.ui.screens.VerseAiAssistantDialog
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ToolVerseViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ToolVerseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            val allTools by viewModel.allTools.collectAsState()
            val searchQuery by viewModel.searchQuery.collectAsState()
            val savedTools by viewModel.savedToolsList.collectAsState()
            val userProjects by viewModel.userProjects.collectAsState()
            val movieReviews by viewModel.movieReviews.collectAsState()
            val adminCustomTools by viewModel.adminCustomTools.collectAsState()
            val userProfile by viewModel.userProfile.collectAsState()
            val aiChatMessages by viewModel.aiChatMessages.collectAsState()
            val isAiThinking by viewModel.isAiThinking.collectAsState()
            val notificationMessage by viewModel.notificationMessage.collectAsState()
            val websiteProject by viewModel.websiteProject.collectAsState()
            val codeContent by viewModel.codeContent.collectAsState()
            val jsonInput by viewModel.jsonInput.collectAsState()
            val jsonValidation by viewModel.jsonValidationResult.collectAsState()
            val regexPattern by viewModel.regexPattern.collectAsState()
            val regexTestText by viewModel.regexTestText.collectAsState()
            val markdownContent by viewModel.markdownContent.collectAsState()
            val socialPostDraft by viewModel.socialPostDraft.collectAsState()

            var showVerseAiDialog by remember { mutableStateOf(false) }

            val navController = rememberNavController()
            val favoriteToolIds = remember(savedTools) {
                savedTools.filter { it.isFavorite }.map { it.toolId }.toSet()
            }

            MyApplicationTheme(darkTheme = isDarkMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        ToolVerseTopBar(
                            isDarkMode = isDarkMode,
                            onToggleDarkMode = { viewModel.toggleDarkMode() },
                            onOpenAiAssistant = { showVerseAiDialog = true },
                            onOpenAdmin = { navController.navigate("admin") },
                            onOpenProfile = { navController.navigate("dashboard") },
                            onSearchClick = { navController.navigate("catalog/all") },
                            onHomeClick = { navController.navigate("home") }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "home",
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // 1. HOME SCREEN
                            composable("home") {
                                HomeScreen(
                                    allTools = allTools,
                                    searchQuery = searchQuery,
                                    onSearchQueryChange = { q ->
                                        viewModel.setSearchQuery(q)
                                        if (q.isNotBlank()) {
                                            navController.navigate("catalog/all")
                                        }
                                    },
                                    favoriteToolIds = favoriteToolIds,
                                    onToggleFavorite = { toolId, cur -> viewModel.toggleFavorite(toolId, cur) },
                                    onOpenTool = { toolId ->
                                        viewModel.recordToolUsage(toolId)
                                        navController.navigate("tool/$toolId")
                                    },
                                    onSelectCategory = { cat ->
                                        navController.navigate("catalog/${cat.id}")
                                    },
                                    onOpenAiAssistant = { showVerseAiDialog = true },
                                    onOpenUpgradeDialog = { navController.navigate("auth") }
                                )
                            }

                            // 2. TOOL CATALOG / MARKETPLACE SCREEN
                            composable(
                                route = "catalog/{categoryId}",
                                arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: "all"
                                val initialCat = ToolCategory.entries.find { it.id == categoryId }

                                ToolCatalogScreen(
                                    initialCategory = initialCat,
                                    allTools = allTools,
                                    favoriteToolIds = favoriteToolIds,
                                    onToggleFavorite = { toolId, cur -> viewModel.toggleFavorite(toolId, cur) },
                                    onOpenTool = { toolId ->
                                        viewModel.recordToolUsage(toolId)
                                        navController.navigate("tool/$toolId")
                                    },
                                    onBack = { navController.popBackStack() }
                                )
                            }

                            // 3. TOOL WORKSPACE SCREEN
                            composable(
                                route = "tool/{toolId}",
                                arguments = listOf(navArgument("toolId") { type = NavType.StringType })
                            ) { backStackEntry ->
                                val toolId = backStackEntry.arguments?.getString("toolId") ?: "website_builder"

                                ToolWorkspaceScreen(
                                    toolId = toolId,
                                    allTools = allTools,
                                    // Website builder
                                    websiteProject = websiteProject,
                                    isAiThinking = isAiThinking,
                                    onGenerateWebsite = { prompt -> viewModel.generateWebsiteFromPrompt(prompt) },
                                    onUpdateThemeColor = { hex -> viewModel.updateWebsiteThemeColor(hex) },
                                    onUpdateFontStyle = { font -> viewModel.updateWebsiteFontStyle(font) },
                                    onUpdateSection = { secId, sec -> viewModel.updateWebsiteSection(secId, sec) },
                                    onRemoveSection = { secId -> viewModel.removeWebsiteSection(secId) },
                                    onAddSection = { type -> viewModel.addWebsiteSection(type) },
                                    onTogglePublish = { viewModel.togglePublishWebsite() },
                                    onSaveProject = { viewModel.saveWebsiteProject() },
                                    // Coding tools
                                    codeContent = codeContent,
                                    onCodeContentChange = { viewModel.updateCodeContent(it) },
                                    jsonInput = jsonInput,
                                    onJsonInputChange = { viewModel.updateJsonInput(it) },
                                    jsonValidation = jsonValidation,
                                    onFormatJson = { viewModel.formatJson() },
                                    onMinifyJson = { viewModel.minifyJson() },
                                    regexPattern = regexPattern,
                                    regexTestText = regexTestText,
                                    onUpdateRegex = { p, t -> viewModel.updateRegex(p, t) },
                                    markdownContent = markdownContent,
                                    onMarkdownContentChange = { viewModel.updateMarkdown(it) },
                                    onAskAiToExplainCode = { code, prompt ->
                                        viewModel.sendVerseChatMessage("Regarding this code:\n$code\nQuestion: $prompt")
                                    },
                                    // Social media
                                    postDraft = socialPostDraft,
                                    onUpdateDraft = { c, p, h -> viewModel.updateSocialDraft(c, p, h) },
                                    onGenerateAiCaption = { topic, tone -> viewModel.generateAiCaption(topic, tone) },
                                    // Movies
                                    movies = viewModel.repository.curatedMovies,
                                    movieReviews = movieReviews,
                                    onToggleWatchlist = { m -> viewModel.toggleMovieWatchlist(m) },
                                    onSaveMovieReview = { id, t, y, p, r, rev ->
                                        viewModel.saveMovieReview(id, t, y, p, r, rev)
                                    },
                                    // Common
                                    onShowNotification = { msg -> viewModel.showNotification(msg) },
                                    onBack = { navController.popBackStack() }
                                )
                            }

                            // 4. USER DASHBOARD SCREEN
                            composable("dashboard") {
                                UserDashboardScreen(
                                    userProfile = userProfile,
                                    projects = userProjects,
                                    savedTools = savedTools,
                                    allTools = allTools,
                                    onOpenTool = { toolId -> navController.navigate("tool/$toolId") },
                                    onDeleteProject = { id -> viewModel.deleteUserProject(id) },
                                    onUpgradePlan = { navController.navigate("auth") },
                                    onBack = { navController.popBackStack() }
                                )
                            }

                            // 5. ADMIN CONTROL PANEL SCREEN
                            composable("admin") {
                                AdminPanelScreen(
                                    allTools = allTools,
                                    adminCustomTools = adminCustomTools,
                                    onAddCustomTool = { name, cat, desc, pro, feat, tags ->
                                        viewModel.addAdminCustomTool(name, cat, desc, pro, feat, tags)
                                    },
                                    onDeleteCustomTool = { id -> viewModel.deleteAdminTool(id) },
                                    onShowNotification = { msg -> viewModel.showNotification(msg) },
                                    onBack = { navController.popBackStack() }
                                )
                            }

                            // 6. AUTH & SUBSCRIPTION SCREEN
                            composable("auth") {
                                AuthScreen(
                                    userProfile = userProfile,
                                    onUpgradeToPro = { viewModel.upgradeToProPlan() },
                                    onDowngradeToFree = { viewModel.downgradeToFreePlan() },
                                    onShowNotification = { msg -> viewModel.showNotification(msg) },
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }

                        // Top Toast Notification Banner
                        NotificationBanner(
                            message = notificationMessage,
                            onDismiss = { viewModel.clearNotification() }
                        )

                        // Verse AI Assistant Dialog
                        if (showVerseAiDialog) {
                            VerseAiAssistantDialog(
                                messages = aiChatMessages,
                                isThinking = isAiThinking,
                                onSendMessage = { text -> viewModel.sendVerseChatMessage(text) },
                                onNavigateToTool = { toolId ->
                                    showVerseAiDialog = false
                                    viewModel.recordToolUsage(toolId)
                                    navController.navigate("tool/$toolId")
                                },
                                onDismiss = { showVerseAiDialog = false }
                            )
                        }
                    }
                }
            }
        }
    }
}
