package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.data.model.SectionType
import com.example.data.model.WebsiteProject
import com.example.data.model.WebsiteSection
import com.example.ui.components.ToolBadge
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldPro
import com.example.ui.theme.VioletSecondary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun WebsiteBuilderScreen(
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
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var promptInput by remember { mutableStateOf(websiteProject.prompt) }
    var isMobilePreview by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Live Preview, 1: Sections Editor, 2: Styles & SEO
    var showExportCodeDialog by remember { mutableStateOf(false) }
    var editingSection by remember { mutableStateOf<WebsiteSection?>(null) }
    var showAddSectionSheet by remember { mutableStateOf(false) }

    val presetColors = listOf("#00E5FF", "#7B2CBF", "#10B981", "#F59E0B", "#EF4444", "#3B82F6", "#EC4899", "#1E293B")
    val fontOptions = listOf("Modern Sans", "Elegant Serif", "Tech Monospace", "Clean Grotesk")

    val parsedThemeColor = try {
        Color(android.graphics.Color.parseColor(websiteProject.themeColorHex))
    } catch (e: Exception) {
        CyanPrimary
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
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("website_builder_back_btn")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "AI Website Builder",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            ToolBadge(text = "NO-CODE", containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
                        }
                        Text(
                            text = if (websiteProject.isPublished) "● Live at ${websiteProject.customDomain}" else "Draft Mode",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (websiteProject.isPublished) EmeraldSuccess else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Export Code Button
                    IconButton(
                        onClick = { showExportCodeDialog = true },
                        modifier = Modifier.testTag("export_website_code_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Code, contentDescription = "Export Code", tint = CyanPrimary)
                    }

                    // Save Project
                    IconButton(onClick = onSaveProject, modifier = Modifier.testTag("save_website_project_btn")) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = "Save Project", tint = VioletSecondary)
                    }

                    // Publish Button
                    Button(
                        onClick = onTogglePublish,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (websiteProject.isPublished) EmeraldSuccess else CyanPrimary,
                            contentColor = if (websiteProject.isPublished) Color.White else Color(0xFF001F29)
                        ),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Icon(
                            imageVector = if (websiteProject.isPublished) Icons.Default.CloudDone else Icons.Default.Public,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (websiteProject.isPublished) "Published" else "Publish",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // PROMPT INPUT BAR
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Describe your desired website:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = promptInput,
                        onValueChange = { promptInput = it },
                        placeholder = { Text("e.g. Create a restaurant website with menu, gallery and online booking") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("website_prompt_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onGenerateWebsite(promptInput) },
                        enabled = !isAiThinking && promptInput.isNotBlank(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VioletSecondary,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .height(52.dp)
                            .testTag("generate_website_btn")
                    ) {
                        if (isAiThinking) {
                            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Generate", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // TABS: LIVE PREVIEW | SECTIONS MANAGER | STYLES & SEO
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = CyanPrimary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Live Preview") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Sections (${websiteProject.sections.size})") }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Styles & Theme") }
            )
        }

        // MAIN CONTENT ACCORDING TO SELECTED TAB
        when (selectedTab) {
            0 -> {
                // LIVE PREVIEW TAB WITH VIEWPORT SWITCHER
                Column(modifier = Modifier.fillMaxSize()) {
                    // Viewport control bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Preview:",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            FilterChip(
                                selected = !isMobilePreview,
                                onClick = { isMobilePreview = false },
                                label = { Text("Desktop") },
                                leadingIcon = { Icon(Icons.Default.Computer, null, Modifier.size(14.dp)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyanPrimary.copy(alpha = 0.2f),
                                    selectedLabelColor = CyanPrimary
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            FilterChip(
                                selected = isMobilePreview,
                                onClick = { isMobilePreview = true },
                                label = { Text("Mobile") },
                                leadingIcon = { Icon(Icons.Default.PhoneAndroid, null, Modifier.size(14.dp)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyanPrimary.copy(alpha = 0.2f),
                                    selectedLabelColor = CyanPrimary
                                )
                            )
                        }

                        Text(
                            text = "${websiteProject.sections.count { it.isVisible }} Sections Active",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Website rendered canvas frame
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(if (isMobilePreview) 320.dp else 600.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .border(2.dp, parsedThemeColor.copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                // Website Navbar Header
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = parsedThemeColor.copy(alpha = 0.1f)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = websiteProject.siteName,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = parsedThemeColor
                                        )
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Text("Home", style = MaterialTheme.typography.labelSmall, color = parsedThemeColor)
                                            Text("Menu", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface)
                                            Text("About", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface)
                                            Text("Contact", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface)
                                        }
                                    }
                                }

                                // Rendered Sections
                                websiteProject.sections.filter { it.isVisible }.forEach { section ->
                                    WebsiteSectionRenderView(
                                        section = section,
                                        themeColor = parsedThemeColor,
                                        onEdit = { editingSection = section }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // SECTIONS MANAGER TAB
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Page Sections & Layout Order",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Button(
                            onClick = { showAddSectionSheet = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
                        ) {
                            Icon(Icons.Default.Add, null, Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Add Section", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(websiteProject.sections) { index, section ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            ToolBadge(
                                                text = section.type.name,
                                                containerColor = parsedThemeColor.copy(alpha = 0.2f),
                                                contentColor = parsedThemeColor
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            Text(
                                                text = section.title,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 1
                                            )
                                        }
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            text = section.subtitle,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = { editingSection = section }) {
                                            Icon(Icons.Default.Edit, "Edit", tint = CyanPrimary)
                                        }
                                        IconButton(onClick = {
                                            onUpdateSection(section.id, section.copy(isVisible = !section.isVisible))
                                        }) {
                                            Icon(
                                                imageVector = if (section.isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                                contentDescription = "Toggle Visibility",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        IconButton(onClick = { onRemoveSection(section.id) }) {
                                            Icon(Icons.Default.Delete, "Delete", tint = Color(0xFFEF4444))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // STYLES & THEME TAB
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Visual Branding & Style Controls",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Theme Color Picker
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Brand Primary Accent Color",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(10.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                presetColors.forEach { hex ->
                                    val color = try { Color(android.graphics.Color.parseColor(hex)) } catch (e: Exception) { CyanPrimary }
                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(color)
                                            .border(
                                                if (websiteProject.themeColorHex.equals(hex, ignoreCase = true)) 3.dp else 1.dp,
                                                if (websiteProject.themeColorHex.equals(hex, ignoreCase = true)) Color.White else Color.Transparent,
                                                CircleShape
                                            )
                                            .clickable { onUpdateThemeColor(hex) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (websiteProject.themeColorHex.equals(hex, ignoreCase = true)) {
                                            Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Font Style
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Typography Family",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(10.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                fontOptions.forEach { font ->
                                    FilterChip(
                                        selected = websiteProject.fontStyle == font,
                                        onClick = { onUpdateFontStyle(font) },
                                        label = { Text(font) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = parsedThemeColor.copy(alpha = 0.2f),
                                            selectedLabelColor = parsedThemeColor
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Publishing & Custom Domain
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Hosting & Custom Domain",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Live URL: https://${websiteProject.customDomain}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = CyanPrimary
                            )
                            Spacer(Modifier.height(10.dp))
                            Button(
                                onClick = onTogglePublish,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (websiteProject.isPublished) EmeraldSuccess else CyanPrimary,
                                    contentColor = if (websiteProject.isPublished) Color.White else Color(0xFF001F29)
                                )
                            ) {
                                Text(if (websiteProject.isPublished) "Website is Live (Click to Unpublish)" else "Deploy to ToolVerse Global Edge")
                            }
                        }
                    }
                }
            }
        }
    }

    // SECTION EDITING DIALOG
    if (editingSection != null) {
        val s = editingSection!!
        var editTitle by remember { mutableStateOf(s.title) }
        var editSubtitle by remember { mutableStateOf(s.subtitle) }
        var editContent by remember { mutableStateOf(s.content) }
        var editButtonText by remember { mutableStateOf(s.buttonText) }
        var editImageUrl by remember { mutableStateOf(s.imageUrl) }

        Dialog(onDismissRequest = { editingSection = null }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .clip(RoundedCornerShape(16.dp)),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Edit ${s.type.name} Section",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        label = { Text("Heading Title") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editSubtitle,
                        onValueChange = { editSubtitle = it },
                        label = { Text("Sub-heading") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editContent,
                        onValueChange = { editContent = it },
                        label = { Text("Body Content") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    OutlinedTextField(
                        value = editButtonText,
                        onValueChange = { editButtonText = it },
                        label = { Text("Call to Action Button") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = editImageUrl,
                        onValueChange = { editImageUrl = it },
                        label = { Text("Hero / Gallery Image URL") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(onClick = { editingSection = null }) {
                            Text("Cancel")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = {
                                onUpdateSection(
                                    s.id,
                                    s.copy(
                                        title = editTitle,
                                        subtitle = editSubtitle,
                                        content = editContent,
                                        buttonText = editButtonText,
                                        imageUrl = editImageUrl
                                    )
                                )
                                editingSection = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
                        ) {
                            Text("Save Changes")
                        }
                    }
                }
            }
        }
    }

    // ADD SECTION BOTTOM SHEET
    if (showAddSectionSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddSectionSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Select a Section Block to Add",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(12.dp))

                SectionType.entries.forEach { type ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                onAddSection(type)
                                showAddSectionSheet = false
                            },
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Add, null, tint = CyanPrimary, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(type.name.replace("_", " "), fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }

    // EXPORT CODE MODAL DIALOG
    if (showExportCodeDialog) {
        val generatedHtmlCode = buildString {
            append("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n")
            append("  <meta charset=\"UTF-8\">\n  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
            append("  <title>${websiteProject.siteName}</title>\n")
            append("  <script src=\"https://cdn.tailwindcss.com\"></script>\n")
            append("  <style>:root { --brand: ${websiteProject.themeColorHex}; }</style>\n")
            append("</head>\n<body class=\"bg-slate-950 text-slate-100 font-sans\">\n")
            append("  <nav class=\"p-6 flex justify-between items-center border-b border-slate-800\">\n")
            append("    <h1 class=\"text-2xl font-bold text-[${websiteProject.themeColorHex}]\">${websiteProject.siteName}</h1>\n")
            append("  </nav>\n")
            websiteProject.sections.filter { it.isVisible }.forEach { sec ->
                append("  <section class=\"py-16 px-6 max-w-5xl mx-auto border-b border-slate-800\">\n")
                append("    <h2 class=\"text-4xl font-extrabold mb-2 text-[${websiteProject.themeColorHex}]\">${sec.title}</h2>\n")
                append("    <h3 class=\"text-xl text-slate-400 mb-6\">${sec.subtitle}</h3>\n")
                append("    <p class=\"text-slate-300 leading-relaxed mb-6\">${sec.content}</p>\n")
                if (sec.buttonText.isNotBlank()) {
                    append("    <button class=\"px-6 py-3 rounded-xl font-bold text-slate-900 bg-[${websiteProject.themeColorHex}] shadow-lg\">${sec.buttonText}</button>\n")
                }
                append("  </section>\n")
            }
            append("  <footer class=\"py-10 text-center text-slate-500 text-sm\">Generated with ToolVerse AI</footer>\n")
            append("</body>\n</html>")
        }

        Dialog(onDismissRequest = { showExportCodeDialog = false }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.8f)
                    .clip(RoundedCornerShape(16.dp)),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Export Website HTML / CSS", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { showExportCodeDialog = false }) {
                            Icon(Icons.Default.Check, "Done")
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)),
                        color = Color(0xFF090D16)
                    ) {
                        Text(
                            text = generatedHtmlCode,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = CyanPrimary,
                            modifier = Modifier
                                .padding(12.dp)
                                .verticalScroll(rememberScrollState())
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    Button(
                        onClick = { showExportCodeDialog = false },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
                    ) {
                        Icon(Icons.Default.ContentCopy, null, Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Copy HTML Code to Clipboard", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun WebsiteSectionRenderView(
    section: WebsiteSection,
    themeColor: Color,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onEdit() }
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            when (section.type) {
                SectionType.HERO -> {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Black,
                        color = themeColor
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = section.subtitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = section.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(12.dp))

                    if (section.imageUrl.isNotBlank()) {
                        AsyncImage(
                            model = section.imageUrl,
                            contentDescription = section.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                        Spacer(Modifier.height(12.dp))
                    }

                    Button(
                        onClick = onEdit,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = themeColor, contentColor = Color(0xFF001F29))
                    ) {
                        Text(section.buttonText, fontWeight = FontWeight.Bold)
                    }
                }

                SectionType.MENU_GALLERY -> {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = themeColor
                    )
                    Text(
                        text = section.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(10.dp))
                    section.items.forEach { item ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = "• $item",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }

                SectionType.FEATURES -> {
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = themeColor
                    )
                    Text(
                        text = section.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                    section.items.forEach { feat ->
                        Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, null, tint = themeColor, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(feat, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                SectionType.CONTACT_BOOKING -> {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = themeColor.copy(alpha = 0.1f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, themeColor.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(section.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = themeColor)
                            Text(section.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(Modifier.height(6.dp))
                            Text(section.content, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.height(10.dp))
                            Button(
                                onClick = onEdit,
                                colors = ButtonDefaults.buttonColors(containerColor = themeColor, contentColor = Color(0xFF001F29)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(section.buttonText, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                else -> {
                    Text(section.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = themeColor)
                    Text(section.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(Modifier.height(4.dp))
                    Text(section.content, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
