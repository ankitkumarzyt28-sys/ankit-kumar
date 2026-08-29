package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SocialPostDraft
import com.example.ui.components.ToolBadge
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldPro
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.VioletSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SocialMediaScreen(
    initialToolId: String? = null,
    postDraft: SocialPostDraft,
    onUpdateDraft: (String, List<String>, List<String>) -> Unit,
    isAiThinking: Boolean,
    onGenerateAiCaption: (String, String) -> Unit,
    onShowNotification: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val subTools = listOf(
        "Social Manager",
        "Post Scheduler",
        "Caption Generator",
        "Hashtag Generator",
        "Social Image Creator",
        "Social Analytics"
    )

    var selectedTabIndex by remember {
        mutableIntStateOf(
            when (initialToolId) {
                "post_scheduler" -> 1
                "caption_gen" -> 2
                "hashtag_gen" -> 3
                "social_img_creator" -> 4
                "social_analytics" -> 5
                else -> 0
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TOP APP BAR
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("social_media_back_btn")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Social Media Growth Hub",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            ToolBadge(text = "SOCIAL MEDIA", containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
                        }
                        Text(
                            text = "Multi-network publishing, AI copywriter, scheduler & growth metrics",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // TABS
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = CyanPrimary,
            edgePadding = 12.dp
        ) {
            subTools.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title, fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal) }
                )
            }
        }

        // TAB VIEWS
        when (selectedTabIndex) {
            0 -> SocialManagerTab(postDraft, onUpdateDraft, onShowNotification)
            1 -> PostSchedulerTab(postDraft, onShowNotification)
            2 -> CaptionGeneratorTab(isAiThinking, onGenerateAiCaption, onShowNotification)
            3 -> HashtagGeneratorTab(onShowNotification)
            4 -> SocialImageCreatorTab(onShowNotification)
            5 -> SocialAnalyticsTab()
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SocialManagerTab(
    postDraft: SocialPostDraft,
    onUpdateDraft: (String, List<String>, List<String>) -> Unit,
    onShowNotification: (String) -> Unit
) {
    var content by remember { mutableStateOf(postDraft.content) }
    val platforms = listOf("X (Twitter)", "Instagram", "LinkedIn", "YouTube", "Threads", "TikTok")
    var selectedPlatforms by remember { mutableStateOf(postDraft.platforms) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Compose Multi-Network Broadcast", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = content,
            onValueChange = {
                content = it
                onUpdateDraft(it, selectedPlatforms, postDraft.hashtags)
            },
            label = { Text("Post Message Copy") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4
        )

        Text("Target Publishing Channels", style = MaterialTheme.typography.labelSmall)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            platforms.forEach { p ->
                val isSelected = selectedPlatforms.contains(p)
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedPlatforms = if (isSelected) selectedPlatforms - p else selectedPlatforms + p
                        onUpdateDraft(content, selectedPlatforms, postDraft.hashtags)
                    },
                    label = { Text(p) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyanPrimary.copy(alpha = 0.2f),
                        selectedLabelColor = CyanPrimary
                    )
                )
            }
        }

        // Live Feed Card Preview
        Text("Multi-Channel Unified Preview", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CyanPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("TV", fontWeight = FontWeight.Bold, color = Color(0xFF001F29))
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("ToolVerse AI Official", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                        Text("Broadcasting to ${selectedPlatforms.size} networks", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(Modifier.height(10.dp))
                Text(content, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)
                Spacer(Modifier.height(8.dp))
                Text(postDraft.hashtags.joinToString(" "), color = CyanPrimary, style = MaterialTheme.typography.labelSmall)
            }
        }

        Button(
            onClick = { onShowNotification("Published across ${selectedPlatforms.joinToString(", ")}!") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
        ) {
            Icon(Icons.Default.Send, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Publish Instantly to ${selectedPlatforms.size} Networks", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PostSchedulerTab(postDraft: SocialPostDraft, onShowNotification: (String) -> Unit) {
    var scheduledDate by remember { mutableStateOf("Tomorrow, 10:00 AM (Optimal Engagement Time)") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Content Release Automation", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text("AI calculated highest audience activity slot: 10:00 AM UTC.", color = EmeraldSuccess, style = MaterialTheme.typography.bodySmall)
            }
        }

        OutlinedTextField(
            value = scheduledDate,
            onValueChange = { scheduledDate = it },
            label = { Text("Scheduled Release Time") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { onShowNotification("Post queued for automated release at $scheduledDate!") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VioletSecondary, contentColor = Color.White)
        ) {
            Icon(Icons.Default.CalendarToday, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Schedule Automated Post", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CaptionGeneratorTab(
    isAiThinking: Boolean,
    onGenerateAiCaption: (String, String) -> Unit,
    onShowNotification: (String) -> Unit
) {
    var topic by remember { mutableStateOf("Launching our new AI web design feature for creators") }
    var tone by remember { mutableStateOf("Viral Hook") }

    val tones = listOf("Viral Hook", "Professional SaaS", "Witty & Fun", "Urgency/Scarcity", "Inspirational")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = topic,
            onValueChange = { topic = it },
            label = { Text("Post Subject or Campaign Goal") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        Text("Tone of Voice", style = MaterialTheme.typography.labelSmall)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tones.forEach { t ->
                FilterChip(
                    selected = tone == t,
                    onClick = { tone = t },
                    label = { Text(t) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyanPrimary.copy(alpha = 0.2f),
                        selectedLabelColor = CyanPrimary
                    )
                )
            }
        }

        Button(
            onClick = { onGenerateAiCaption(topic, tone) },
            enabled = !isAiThinking,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VioletSecondary, contentColor = Color.White)
        ) {
            if (isAiThinking) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
            } else {
                Icon(Icons.Default.AutoAwesome, null, Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Generate AI High-Converting Caption", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HashtagGeneratorTab(onShowNotification: (String) -> Unit) {
    var niche by remember { mutableStateOf("Artificial Intelligence SaaS") }
    val tags = listOf("#ToolVerseAI", "#AIProductivity", "#SaaSGrowth", "#TechInnovation", "#BuildInPublic", "#CreatorEconomy", "#CodingLife", "#DesignSystem")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = niche,
            onValueChange = { niche = it },
            label = { Text("Target Niche / Industry") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("High-Reach Tag Clusters", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tags.forEach { tag ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CyanPrimary.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.3f))
                ) {
                    Text(tag, color = CyanPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Button(
            onClick = { onShowNotification("Copied 8 High-Volume Hashtags to Clipboard!") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
        ) {
            Icon(Icons.Default.ContentCopy, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Copy All Hashtags", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SocialImageCreatorTab(onShowNotification: (String) -> Unit) {
    var quoteText by remember { mutableStateOf("“Your focus determines your reality. Ship daily with ToolVerse.”") }
    var author by remember { mutableStateOf("ToolVerse Creator Insights") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(value = quoteText, onValueChange = { quoteText = it }, label = { Text("Quote / Callout Text") }, modifier = Modifier.fillMaxWidth())

        // 1:1 Quote Graphic Canvas
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, CyanPrimary.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF090D16))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(quoteText, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = CyanPrimary)
                Spacer(Modifier.height(12.dp))
                Text("— $author", color = Color(0xFF94A3B8), style = MaterialTheme.typography.labelSmall)
            }
        }

        Button(
            onClick = { onShowNotification("Quote Card Rendered for Instagram Carousel!") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
        ) {
            Icon(Icons.Default.Image, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Download 1080x1080 Carousel Card", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SocialAnalyticsTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("30-Day Growth Telemetry", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Total Impressions", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("248.5K", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = CyanPrimary)
                    Text("+24.8% vs last month", style = MaterialTheme.typography.labelSmall, color = EmeraldSuccess)
                }
            }

            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Engagement Rate", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("6.84%", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = VioletSecondary)
                    Text("+1.4% vs industry avg", style = MaterialTheme.typography.labelSmall, color = EmeraldSuccess)
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Channel Performance Breakdown", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("• X (Twitter): 112K views • 4.2K likes", style = MaterialTheme.typography.bodySmall)
                Text("• LinkedIn: 84K views • 890 shares", style = MaterialTheme.typography.bodySmall)
                Text("• Instagram: 52.5K views • 3.1K saves", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
