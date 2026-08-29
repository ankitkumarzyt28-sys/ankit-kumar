package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.entities.AdminCustomToolEntity
import com.example.data.model.ToolCategory
import com.example.data.model.ToolDefinition
import com.example.ui.components.ToolBadge
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldPro
import com.example.ui.theme.VioletSecondary

@Composable
fun AdminPanelScreen(
    allTools: List<ToolDefinition>,
    adminCustomTools: List<AdminCustomToolEntity>,
    onAddCustomTool: (String, String, String, Boolean, Boolean, String) -> Unit,
    onDeleteCustomTool: (String) -> Unit,
    onShowNotification: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddToolDialog by remember { mutableStateOf(false) }

    val tabs = listOf(
        "Catalog Tools (${allTools.size})",
        "Add New Tool",
        "Category Manager",
        "User Analytics",
        "API Settings & Logs"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TOP BAR
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
                    IconButton(onClick = onBack, modifier = Modifier.testTag("admin_back_btn")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Admin Control Center",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            ToolBadge(text = "SUPERADMIN", containerColor = Color(0xFFEF4444), contentColor = Color.White)
                        }
                        Text(
                            text = "Manage marketplace tools, categories, telemetry & API keys",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // TABS
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = CyanPrimary,
            edgePadding = 12.dp
        ) {
            tabs.forEachIndexed { idx, title ->
                Tab(
                    selected = selectedTab == idx,
                    onClick = { selectedTab = idx },
                    text = { Text(title, fontWeight = if (selectedTab == idx) FontWeight.Bold else FontWeight.Normal) }
                )
            }
        }

        when (selectedTab) {
            0 -> {
                // Catalog tools
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(allTools) { tool ->
                        val isCustom = adminCustomTools.any { it.id == tool.id }
                        Card(
                            modifier = Modifier.fillMaxWidth(),
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
                                        Text(tool.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                                        Spacer(Modifier.width(6.dp))
                                        if (tool.isPro) ToolBadge(text = "PRO", containerColor = GoldPro, contentColor = Color.Black)
                                        if (isCustom) ToolBadge(text = "CUSTOM", containerColor = VioletSecondary, contentColor = Color.White)
                                    }
                                    Text(
                                        text = "${tool.category.title} • ID: ${tool.id}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (isCustom) {
                                    IconButton(onClick = { onDeleteCustomTool(tool.id) }) {
                                        Icon(Icons.Default.Delete, "Delete", tint = Color(0xFFEF4444))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Add New Tool Form
                var newToolName by remember { mutableStateOf("") }
                var selectedCat by remember { mutableStateOf(ToolCategory.CODING_TOOLS) }
                var newToolDesc by remember { mutableStateOf("") }
                var isPro by remember { mutableStateOf(false) }
                var isFeatured by remember { mutableStateOf(true) }
                var tags by remember { mutableStateOf("ai, productivity, custom") }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Register New Tool in Marketplace", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                    OutlinedTextField(
                        value = newToolName,
                        onValueChange = { newToolName = it },
                        label = { Text("Tool Display Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Tool Category", style = MaterialTheme.typography.labelSmall)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ToolCategory.entries.take(3).forEach { cat ->
                            FilterChip(
                                selected = selectedCat == cat,
                                onClick = { selectedCat = cat },
                                label = { Text(cat.title, fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CyanPrimary.copy(alpha = 0.2f), selectedLabelColor = CyanPrimary)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = newToolDesc,
                        onValueChange = { newToolDesc = it },
                        label = { Text("Tool Description") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    OutlinedTextField(
                        value = tags,
                        onValueChange = { tags = it },
                        label = { Text("Search Tags (comma separated)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isPro, onCheckedChange = { isPro = it })
                        Spacer(Modifier.width(6.dp))
                        Text("PRO Tier Restricted", style = MaterialTheme.typography.bodySmall)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isFeatured, onCheckedChange = { isFeatured = it })
                        Spacer(Modifier.width(6.dp))
                        Text("Featured on Homepage Carousel", style = MaterialTheme.typography.bodySmall)
                    }

                    Button(
                        onClick = {
                            if (newToolName.isNotBlank()) {
                                onAddCustomTool(newToolName, selectedCat.id, newToolDesc, isPro, isFeatured, tags)
                                newToolName = ""
                                newToolDesc = ""
                                onShowNotification("Tool added to live marketplace!")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
                    ) {
                        Icon(Icons.Default.Add, null, Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Publish Custom Tool", fontWeight = FontWeight.Bold)
                    }
                }
            }

            2 -> {
                // Category Manager
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(ToolCategory.entries) { cat ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(cat.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                                    Text("Category ID: ${cat.id}", style = MaterialTheme.typography.labelSmall, color = CyanPrimary)
                                }
                                ToolBadge(text = "${allTools.count { it.category == cat }} Tools", containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }

            3 -> {
                // User Analytics
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Platform Telemetry & Metrics", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Registered Users", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("18,420", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = CyanPrimary)
                                Text("+312 today", style = MaterialTheme.typography.labelSmall, color = EmeraldSuccess)
                            }
                        }

                        Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text("Active Pro Subscriptions", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("3,140", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = GoldPro)
                                Text("MRR: $59,660", style = MaterialTheme.typography.labelSmall, color = GoldPro)
                            }
                        }
                    }

                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Most Popular Tools Today", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Text("1. AI Website Builder (8,420 sessions)", style = MaterialTheme.typography.bodySmall)
                            Text("2. Background Remover (6,190 sessions)", style = MaterialTheme.typography.bodySmall)
                            Text("3. Online Code Editor (4,810 sessions)", style = MaterialTheme.typography.bodySmall)
                            Text("4. Caption Generator (3,920 sessions)", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            4 -> {
                // API Settings & Logs
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("API Gateway Configuration & Status", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Key, null, tint = CyanPrimary)
                                Spacer(Modifier.width(8.dp))
                                Text("Gemini 3.5 Flash Model Gateway", fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.height(6.dp))
                            Text("API Status: Healthy (Latency ~280ms)", color = EmeraldSuccess, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                            Text("Model: gemini-3.5-flash (with offline fallback engine active)", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelSmall)
                        }
                    }

                    Text("System Event Logs", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF090D16))
                    ) {
                        Text(
                            text = """[INFO] 10:42:01 - ToolVerse Cluster Initialized in us-central1
[INFO] 10:42:02 - Room Database schema verified (4 tables loaded)
[INFO] 10:42:04 - 35 built-in tool definitions registered
[INFO] 10:42:05 - Verse AI assistant agent listening on channel #verse-copilot
[SUCCESS] 10:42:08 - SSL / TLS 1.3 edge handshake established""",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = CyanPrimary,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}
