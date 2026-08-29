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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ClosedCaption
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PhotoSizeSelectActual
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Transform
import androidx.compose.material.icons.filled.Videocam
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
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.components.ToolBadge
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldPro
import com.example.ui.theme.VioletSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoToolsScreen(
    initialToolId: String? = null,
    onShowNotification: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val subTools = listOf(
        "Video Editor",
        "Video Trimmer",
        "Video Compressor",
        "Video Converter",
        "Subtitle Generator",
        "AI Video Script",
        "Thumbnail Maker",
        "Reel/Shorts Maker"
    )

    var selectedTabIndex by remember {
        mutableIntStateOf(
            when (initialToolId) {
                "video_trimmer" -> 1
                "video_compressor" -> 2
                "video_converter" -> 3
                "subtitle_gen" -> 4
                "ai_video_gen" -> 5
                "thumbnail_maker" -> 6
                "reels_shorts_maker" -> 7
                else -> 0
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Bar
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
                    IconButton(onClick = onBack, modifier = Modifier.testTag("video_tools_back_btn")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Video & Motion Studio",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            ToolBadge(text = "VIDEO TOOLS", containerColor = VioletSecondary, contentColor = Color.White)
                        }
                        Text(
                            text = "Timeline editor, subtitle syncer, compressor & 4K video converters",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Tabs
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

        // Subtool View
        when (selectedTabIndex) {
            0 -> VideoEditorWorkspace(onShowNotification)
            1 -> VideoTrimmerWorkspace(onShowNotification)
            2 -> VideoCompressorWorkspace(onShowNotification)
            3 -> VideoConverterWorkspace(onShowNotification)
            4 -> SubtitleGeneratorWorkspace(onShowNotification)
            5 -> AiVideoScriptWorkspace(onShowNotification)
            6 -> ThumbnailMakerWorkspace(onShowNotification)
            7 -> ReelsShortsMakerWorkspace(onShowNotification)
        }
    }
}

@Composable
fun VideoEditorWorkspace(onShowNotification: (String) -> Unit) {
    var playbackPosition by remember { mutableFloatStateOf(14.5f) }
    var speed by remember { mutableFloatStateOf(1.0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Video Preview Monitor
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CyanPrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF090D16))
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=800",
                    contentDescription = "Video Monitor",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.Black.copy(alpha = 0.7f))
                ) {
                    Icon(Icons.Default.PlayArrow, null, tint = CyanPrimary, modifier = Modifier.size(28.dp))
                }
            }
        }

        // Timeline Tracks
        Text("Multi-Track Timeline (00:00:14 / 00:01:30)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D111A))
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(28.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = CyanPrimary.copy(alpha = 0.3f)
                ) {
                    Text("🎬 Video Clip A (Main Track)", color = CyanPrimary, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(6.dp))
                }
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(28.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = VioletSecondary.copy(alpha = 0.3f)
                ) {
                    Text("🎵 Audio Waveform (Stereo 48kHz)", color = VioletSecondary, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(6.dp))
                }
            }
        }

        Button(
            onClick = { onShowNotification("Rendered 1080p 60fps MP4 Video!") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
        ) {
            Icon(Icons.Default.Videocam, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Export Final Video Project", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun VideoTrimmerWorkspace(onShowNotification: (String) -> Unit) {
    var trimStart by remember { mutableFloatStateOf(5.0f) }
    var trimEnd by remember { mutableFloatStateOf(45.0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Precision Lossless Cut", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("Trim Start: 00:0${trimStart.toInt()}s | Trim End: 00:${trimEnd.toInt()}s", color = CyanPrimary, fontWeight = FontWeight.Bold)

        Slider(value = trimStart, onValueChange = { trimStart = it.coerceAtMost(trimEnd - 1f) }, valueRange = 0f..60f)
        Slider(value = trimEnd, onValueChange = { trimEnd = it.coerceAtLeast(trimStart + 1f) }, valueRange = 0f..60f)

        Button(
            onClick = { onShowNotification("Lossless Video Cut Exported (${(trimEnd - trimStart).toInt()}s Duration)!") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
        ) {
            Icon(Icons.Default.ContentCut, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Trim & Export Clip", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun VideoCompressorWorkspace(onShowNotification: (String) -> Unit) {
    var resolution by remember { mutableStateOf("1080p (Full HD)") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Video Compression Estimate", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text("Original: 142 MB (4K ProRes) -> Target: 24.5 MB (H.264 Web-Ready)", color = EmeraldSuccess, fontWeight = FontWeight.Bold)
            }
        }

        Button(
            onClick = { onShowNotification("Video Compressed! Reduced by 82% file size.") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
        ) {
            Icon(Icons.Default.Compress, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Compress Video", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun VideoConverterWorkspace(onShowNotification: (String) -> Unit) {
    var selectedFormat by remember { mutableStateOf("MP4") }
    val formats = listOf("MP4", "WebM", "MKV", "AVI", "Animated GIF")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Convert Video Stream Container", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            formats.forEach { fmt ->
                FilterChip(
                    selected = selectedFormat == fmt,
                    onClick = { selectedFormat = fmt },
                    label = { Text(fmt) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CyanPrimary.copy(alpha = 0.2f), selectedLabelColor = CyanPrimary)
                )
            }
        }

        Button(
            onClick = { onShowNotification("Converted to $selectedFormat!") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
        ) {
            Icon(Icons.Default.Transform, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Convert & Download", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SubtitleGeneratorWorkspace(onShowNotification: (String) -> Unit) {
    var subtitleText by remember {
        mutableStateOf(
            """1
00:00:01,200 --> 00:00:04,500
Welcome to ToolVerse AI, your complete digital tool platform.

2
00:00:04,800 --> 00:00:08,900
Generate websites, code, and edit multimedia with one subscription."""
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Auto-Transcribed SRT Subtitles", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = subtitleText,
            onValueChange = { subtitleText = it },
            modifier = Modifier.fillMaxWidth(),
            minLines = 6,
            textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace, color = CyanPrimary)
        )

        Button(
            onClick = { onShowNotification("Exported subtitles.srt file!") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
        ) {
            Icon(Icons.Default.ClosedCaption, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Download .SRT File", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AiVideoScriptWorkspace(onShowNotification: (String) -> Unit) {
    var topic by remember { mutableStateOf("Why ToolVerse AI replaces 10 separate software subscriptions") }
    var scriptOutput by remember {
        mutableStateOf(
            """[HOOK - 0:00-0:03]: "Stop paying for 10 different SaaS tools."
[PROBLEM - 0:03-0:08]: Showing cluttered browser tabs and expensive bills.
[SOLUTION - 0:08-0:18]: ToolVerse AI generates websites, edits 4K video, formats code and manages social channels.
[CTA - 0:18-0:25]: "Claim your free tier on ToolVerse today!""""
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(value = topic, onValueChange = { topic = it }, label = { Text("Video Topic or Concept") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = { onShowNotification("AI Video Script & Storyboard Generated!") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VioletSecondary, contentColor = Color.White)
        ) {
            Icon(Icons.Default.AutoAwesome, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Generate Viral Storyboard", fontWeight = FontWeight.Bold)
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D111A))
        ) {
            Text(
                text = scriptOutput,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                color = Color(0xFFE2E8F0),
                modifier = Modifier.padding(14.dp),
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun ThumbnailMakerWorkspace(onShowNotification: (String) -> Unit) {
    var titleText by remember { mutableStateOf("NEW IN 2026!") }
    var badgeText by remember { mutableStateOf("MUST WATCH") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(value = titleText, onValueChange = { titleText = it }, label = { Text("Bold Thumbnail Title") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = badgeText, onValueChange = { badgeText = it }, label = { Text("Attention Badge") }, modifier = Modifier.fillMaxWidth())

        // 16:9 Thumbnail Preview Canvas
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(2.dp, CyanPrimary, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF090D16))
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1635805737707-575885ab0820?w=800",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(12.dp)
                ) {
                    ToolBadge(text = badgeText, containerColor = Color(0xFFEF4444), contentColor = Color.White)
                    Spacer(Modifier.height(6.dp))
                    Text(titleText, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Black, color = CyanPrimary)
                }
            }
        }

        Button(
            onClick = { onShowNotification("High-CTR 1280x720 Thumbnail Exported!") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
        ) {
            Icon(Icons.Default.PhotoSizeSelectActual, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Export YouTube Thumbnail (1280x720)", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ReelsShortsMakerWorkspace(onShowNotification: (String) -> Unit) {
    var captionSticker by remember { mutableStateOf("⚡ Don't miss this update!") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(value = captionSticker, onValueChange = { captionSticker = it }, label = { Text("Shorts Caption Sticker") }, modifier = Modifier.fillMaxWidth())

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .width(160.dp)
                    .height(280.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(2.dp, VioletSecondary, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF090D16))
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Black.copy(alpha = 0.8f)
                    ) {
                        Text(captionSticker, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(6.dp))
                    }
                }
            }
        }

        Button(
            onClick = { onShowNotification("9:16 Vertical Reel Rendered for TikTok & Instagram!") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VioletSecondary, contentColor = Color.White)
        ) {
            Icon(Icons.Default.SmartDisplay, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Export Vertical Reel (1080x1920)", fontWeight = FontWeight.Bold)
        }
    }
}
