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
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Compress
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LayersClear
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhotoFilter
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.material3.SliderDefaults
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
fun PhotoToolsScreen(
    initialToolId: String? = null,
    onShowNotification: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val subTools = listOf(
        "Photo Editor",
        "Background Remover",
        "AI Image Gen",
        "Image Compressor",
        "Image Resizer",
        "Image Converter",
        "Logo Maker",
        "Poster Maker"
    )

    var selectedTabIndex by remember {
        mutableIntStateOf(
            when (initialToolId) {
                "bg_remover" -> 1
                "ai_image_gen" -> 2
                "img_compressor" -> 3
                "img_resizer" -> 4
                "img_converter" -> 5
                "logo_maker" -> 6
                "poster_maker" -> 7
                else -> 0
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
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
                    IconButton(onClick = onBack, modifier = Modifier.testTag("photo_tools_back_btn")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Photo & Graphics Suite",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            ToolBadge(text = "PHOTO TOOLS", containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
                        }
                        Text(
                            text = "AI background removal, filter studio, vector logos & lossless compression",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Sub Tools Tab Row
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

        // Active Workspace
        when (selectedTabIndex) {
            0 -> PhotoEditorWorkspace(onShowNotification)
            1 -> BackgroundRemoverWorkspace(onShowNotification)
            2 -> AiImageGenWorkspace(onShowNotification)
            3 -> ImageCompressorWorkspace(onShowNotification)
            4 -> ImageResizerWorkspace(onShowNotification)
            5 -> ImageConverterWorkspace(onShowNotification)
            6 -> LogoMakerWorkspace(onShowNotification)
            7 -> PosterMakerWorkspace(onShowNotification)
        }
    }
}

@Composable
fun PhotoEditorWorkspace(onShowNotification: (String) -> Unit) {
    var brightness by remember { mutableFloatStateOf(1f) }
    var contrast by remember { mutableFloatStateOf(1f) }
    var saturation by remember { mutableFloatStateOf(1f) }
    var selectedFilter by remember { mutableStateOf("Original") }

    val filters = listOf("Original", "Cyberpunk Cyan", "Vintage Warm", "B&W Film", "Vibrant HDR", "Moody Dark")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Image Canvas Preview
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CyanPrimary.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF090D16))
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=800",
                    contentDescription = "Editing Canvas",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Black.copy(alpha = 0.7f)
                ) {
                    Text(
                        text = "Filter: $selectedFilter • B:${(brightness * 100).toInt()}% • C:${(contrast * 100).toInt()}%",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Filter Presets
        Text("Color Grading LUT Presets", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filters.forEach { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyanPrimary.copy(alpha = 0.2f),
                        selectedLabelColor = CyanPrimary
                    )
                )
            }
        }

        // Sliders
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Brightness (${(brightness * 100).toInt()}%)", style = MaterialTheme.typography.labelSmall)
            Slider(value = brightness, onValueChange = { brightness = it }, valueRange = 0.5f..2.0f)

            Text("Contrast (${(contrast * 100).toInt()}%)", style = MaterialTheme.typography.labelSmall)
            Slider(value = contrast, onValueChange = { contrast = it }, valueRange = 0.5f..2.0f)

            Text("Saturation (${(saturation * 100).toInt()}%)", style = MaterialTheme.typography.labelSmall)
            Slider(value = saturation, onValueChange = { saturation = it }, valueRange = 0.0f..2.5f)
        }

        // Export
        Button(
            onClick = { onShowNotification("High-Res Edited Image Exported to Gallery!") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
        ) {
            Icon(Icons.Default.Download, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Export Enhanced Image (PNG 4K)", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun BackgroundRemoverWorkspace(onShowNotification: (String) -> Unit) {
    var isProcessing by remember { mutableStateOf(false) }
    var isRemoved by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, VioletSecondary.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = if (isRemoved) Color(0xFF1E293B) else Color(0xFF090D16))
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (isRemoved) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.LayersClear, null, tint = CyanPrimary, modifier = Modifier.size(48.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Subject Isolated (Transparent Alpha Channel)", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Background pixels cleared with sub-pixel edge feathering", color = Color(0xFF94A3B8), style = MaterialTheme.typography.bodySmall)
                    }
                } else {
                    AsyncImage(
                        model = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=800",
                        contentDescription = "Original Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
                onClick = { isRemoved = !isRemoved },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(if (isRemoved) "View Original" else "View Cutout")
            }
            Button(
                onClick = { onShowNotification("Transparent PNG Downloaded Successfully!") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
            ) {
                Icon(Icons.Default.Download, null, Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Export PNG", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AiImageGenWorkspace(onShowNotification: (String) -> Unit) {
    var prompt by remember { mutableStateOf("Futuristic neon cybernetic workspace with holograms and ultra-wide monitor setup") }
    var selectedStyle by remember { mutableStateOf("Photorealistic") }
    var isGenerating by remember { mutableStateOf(false) }

    val styles = listOf("Photorealistic", "Cyberpunk", "3D Render", "Anime Aesthetic", "Vector Mark", "Oil Painting")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("AI Diffusion Prompt Studio", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            label = { Text("Visual Prompt Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        Text("Artistic Style Preset", style = MaterialTheme.typography.labelSmall)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            styles.forEach { s ->
                FilterChip(
                    selected = selectedStyle == s,
                    onClick = { selectedStyle = s },
                    label = { Text(s) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = VioletSecondary.copy(alpha = 0.2f),
                        selectedLabelColor = VioletSecondary
                    )
                )
            }
        }

        Button(
            onClick = {
                isGenerating = true
                onShowNotification("AI Image Synthesized in $selectedStyle Style!")
                isGenerating = false
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VioletSecondary, contentColor = Color.White)
        ) {
            Icon(Icons.Default.AutoAwesome, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Generate Artwork with AI", fontWeight = FontWeight.Bold)
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF090D16))
        ) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=800",
                contentDescription = "AI Generated Artwork",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun ImageCompressorWorkspace(onShowNotification: (String) -> Unit) {
    var compressionQuality by remember { mutableFloatStateOf(75f) }
    var isLossless by remember { mutableStateOf(false) }

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
                Text("Compression Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Original Size:", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("4.8 MB", fontWeight = FontWeight.Bold)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Estimated Compressed Size:", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${(4.8 * (compressionQuality / 100)).toString().take(4)} MB (-${(100 - compressionQuality).toInt()}%)", color = EmeraldSuccess, fontWeight = FontWeight.Bold)
                }
            }
        }

        Text("Quality Target: ${compressionQuality.toInt()}%", style = MaterialTheme.typography.titleSmall)
        Slider(value = compressionQuality, onValueChange = { compressionQuality = it }, valueRange = 10f..95f)

        Button(
            onClick = { onShowNotification("Compressed Image Saved! Saved ${(100 - compressionQuality).toInt()}% space.") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
        ) {
            Icon(Icons.Default.Compress, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Compress & Download Image", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ImageResizerWorkspace(onShowNotification: (String) -> Unit) {
    var widthPx by remember { mutableStateOf("1080") }
    var heightPx by remember { mutableStateOf("1080") }
    var selectedPreset by remember { mutableStateOf("Instagram Square (1080x1080)") }

    val presets = listOf(
        "Instagram Square (1080x1080)",
        "YouTube Cover (1280x720)",
        "Story/Reels (1080x1920)",
        "Twitter Header (1500x500)"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Dimension Presets", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        presets.forEach { p ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        selectedPreset = p
                        if (p.contains("1080x1080")) { widthPx = "1080"; heightPx = "1080" }
                        if (p.contains("1280x720")) { widthPx = "1280"; heightPx = "720" }
                        if (p.contains("1080x1920")) { widthPx = "1080"; heightPx = "1920" }
                        if (p.contains("1500x500")) { widthPx = "1500"; heightPx = "500" }
                    },
                color = if (selectedPreset == p) CyanPrimary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(p, modifier = Modifier.padding(12.dp), fontWeight = if (selectedPreset == p) FontWeight.Bold else FontWeight.Normal)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(value = widthPx, onValueChange = { widthPx = it }, label = { Text("Width (px)") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = heightPx, onValueChange = { heightPx = it }, label = { Text("Height (px)") }, modifier = Modifier.weight(1f))
        }

        Button(
            onClick = { onShowNotification("Resized image to ${widthPx}x${heightPx} px!") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
        ) {
            Text("Resize and Save Image", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ImageConverterWorkspace(onShowNotification: (String) -> Unit) {
    var targetFormat by remember { mutableStateOf("WebP") }
    val formats = listOf("WebP", "PNG", "JPEG", "SVG", "BMP", "ICO")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Target Output Format", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            formats.forEach { f ->
                FilterChip(
                    selected = targetFormat == f,
                    onClick = { targetFormat = f },
                    label = { Text(f) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = CyanPrimary.copy(alpha = 0.2f),
                        selectedLabelColor = CyanPrimary
                    )
                )
            }
        }

        Button(
            onClick = { onShowNotification("Converted to .$targetFormat successfully!") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
        ) {
            Icon(Icons.Default.SwapHoriz, null, Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Convert to $targetFormat", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LogoMakerWorkspace(onShowNotification: (String) -> Unit) {
    var brandName by remember { mutableStateOf("ToolVerse") }
    var slogan by remember { mutableStateOf("Digital Intelligence") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(value = brandName, onValueChange = { brandName = it }, label = { Text("Brand Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = slogan, onValueChange = { slogan = it }, label = { Text("Tagline / Slogan") }, modifier = Modifier.fillMaxWidth())

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0B0F19))
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.linearGradient(listOf(CyanPrimary, VioletSecondary))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(32.dp))
                }
                Spacer(Modifier.height(8.dp))
                Text(brandName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, color = Color.White)
                Text(slogan, style = MaterialTheme.typography.labelSmall, color = CyanPrimary)
            }
        }

        Button(
            onClick = { onShowNotification("Logo Vector (SVG + PNG) Exported!") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
        ) {
            Text("Download Vector Logo Package", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PosterMakerWorkspace(onShowNotification: (String) -> Unit) {
    var headline by remember { mutableStateOf("GLOBAL TECH SUMMIT 2026") }
    var subtitle by remember { mutableStateOf("San Francisco • October 24-26") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(value = headline, onValueChange = { headline = it }, label = { Text("Event Headline") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = subtitle, onValueChange = { subtitle = it }, label = { Text("Date & Location") }, modifier = Modifier.fillMaxWidth())

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2E))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(headline, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = CyanPrimary, textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = Color.White, textAlign = TextAlign.Center)
            }
        }

        Button(
            onClick = { onShowNotification("High-Res Print Poster Rendered!") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
        ) {
            Text("Export Print-Ready Poster", fontWeight = FontWeight.Bold)
        }
    }
}
