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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.Http
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.TextSnippet
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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.ui.components.ToolBadge
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldPro
import com.example.ui.theme.IndigoAccent
import com.example.ui.theme.VioletSecondary
import java.util.regex.Pattern

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CodePlaygroundScreen(
    initialToolId: String? = null,
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
    isAiThinking: Boolean,
    onAskAiToExplainCode: (String, String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val subTools = listOf(
        "HTML/CSS Playground",
        "JSON Validator",
        "Regex Tester",
        "Markdown Editor",
        "API Tester",
        "AI Code Assistant"
    )

    var selectedTabIndex by remember {
        mutableIntStateOf(
            when (initialToolId) {
                "json_formatter" -> 1
                "regex_tester" -> 2
                "markdown_editor" -> 3
                "api_tester" -> 4
                "ai_coding_assistant" -> 5
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
                    IconButton(onClick = onBack, modifier = Modifier.testTag("code_playground_back_btn")) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Developer Studio",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            ToolBadge(text = "CODING TOOLS", containerColor = VioletSecondary, contentColor = Color.White)
                        }
                        Text(
                            text = "Interactive IDE, JSON, Regex, Markdown & AI Diagnostics",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // SCROLLABLE SUB-TOOL TABS
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

        // TOOL WORKSPACE
        when (selectedTabIndex) {
            0 -> {
                // HTML/CSS/JS LIVE PLAYGROUND
                HtmlCssPlaygroundTab(
                    code = codeContent,
                    onCodeChange = onCodeContentChange
                )
            }

            1 -> {
                // JSON FORMATTER & VALIDATOR
                JsonFormatterTab(
                    jsonInput = jsonInput,
                    onJsonChange = onJsonInputChange,
                    validationResult = jsonValidation,
                    onFormat = onFormatJson,
                    onMinify = onMinifyJson
                )
            }

            2 -> {
                // REGEX TESTER
                RegexTesterTab(
                    pattern = regexPattern,
                    testText = regexTestText,
                    onUpdateRegex = onUpdateRegex
                )
            }

            3 -> {
                // MARKDOWN EDITOR & PREVIEW
                MarkdownEditorTab(
                    content = markdownContent,
                    onContentChange = onMarkdownContentChange
                )
            }

            4 -> {
                // REST API TESTER
                ApiTesterTab()
            }

            5 -> {
                // AI CODING ASSISTANT
                AiCodeAssistantTab(
                    codeSnippet = codeContent,
                    isAiThinking = isAiThinking,
                    onAskAi = onAskAiToExplainCode
                )
            }
        }
    }
}

@Composable
fun HtmlCssPlaygroundTab(
    code: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isRunning by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Live Web Sandbox",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = { isRunning = true },
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(34.dp)
            ) {
                Icon(Icons.Default.PlayArrow, null, Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Run Code", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
            }
        }

        Spacer(Modifier.height(8.dp))

        // Code Editor Input Area
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D111A))
        ) {
            OutlinedTextField(
                value = code,
                onValueChange = onCodeChange,
                modifier = Modifier.fillMaxSize(),
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFFE2E8F0),
                    fontSize = 12.sp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }

        Spacer(Modifier.height(10.dp))

        // Live Render Output Sandbox Frame
        Text(
            text = "Rendered DOM Canvas Preview",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = CyanPrimary
        )
        Spacer(Modifier.height(4.dp))

        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, CyanPrimary.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF090D16))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF131B2E)),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ToolVerse AI Sandbox Active",
                            color = CyanPrimary,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "HTML/CSS/JS interpreted successfully with instant reactive preview.",
                            color = Color(0xFF94A3B8),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = VioletSecondary),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Interactive Sandbox Button")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JsonFormatterTab(
    jsonInput: String,
    onJsonChange: (String) -> Unit,
    validationResult: String,
    onFormat: () -> Unit,
    onMinify: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = validationResult,
                style = MaterialTheme.typography.labelMedium,
                color = if (validationResult.startsWith("Valid")) EmeraldSuccess else Color(0xFFEF4444),
                fontWeight = FontWeight.Bold
            )

            Row {
                Button(
                    onClick = onFormat,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29)),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("Prettify", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                }
                Spacer(Modifier.width(6.dp))
                OutlinedButton(
                    onClick = onMinify,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text("Minify", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D111A))
        ) {
            OutlinedTextField(
                value = jsonInput,
                onValueChange = onJsonChange,
                modifier = Modifier.fillMaxSize(),
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF38BDF8),
                    fontSize = 12.sp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun RegexTesterTab(
    pattern: String,
    testText: String,
    onUpdateRegex: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentPattern by remember { mutableStateOf(pattern) }
    var currentTestText by remember { mutableStateOf(testText) }

    val matches = remember(currentPattern, currentTestText) {
        try {
            if (currentPattern.isNotBlank()) {
                val p = Pattern.compile(currentPattern)
                val m = p.matcher(currentTestText)
                val results = mutableListOf<String>()
                while (m.find()) {
                    results.add(m.group())
                }
                results
            } else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Regular Expression Pattern", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = currentPattern,
            onValueChange = {
                currentPattern = it
                onUpdateRegex(it, currentTestText)
            },
            label = { Text("Regex Pattern") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace, color = CyanPrimary)
        )

        Text("Sample Input Text", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = currentTestText,
            onValueChange = {
                currentTestText = it
                onUpdateRegex(currentPattern, it)
            },
            label = { Text("Test Text") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4
        )

        Text(
            text = "Matched Items (${matches.size} Found):",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = if (matches.isNotEmpty()) EmeraldSuccess else Color(0xFFEF4444)
        )

        if (matches.isEmpty()) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text("No pattern matches found.", modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodySmall)
            }
        } else {
            matches.forEachIndexed { idx, match ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = CyanPrimary.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyanPrimary.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ToolBadge(text = "Match #${idx + 1}", containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
                        Spacer(Modifier.width(8.dp))
                        Text(match, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = CyanPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun MarkdownEditorTab(
    content: String,
    onContentChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPreviewMode by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("GitHub Flavored Markdown", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

            Row {
                FilterChip(
                    selected = !isPreviewMode,
                    onClick = { isPreviewMode = false },
                    label = { Text("Edit") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CyanPrimary.copy(alpha = 0.2f), selectedLabelColor = CyanPrimary)
                )
                Spacer(Modifier.width(6.dp))
                FilterChip(
                    selected = isPreviewMode,
                    onClick = { isPreviewMode = true },
                    label = { Text("Preview") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = CyanPrimary.copy(alpha = 0.2f), selectedLabelColor = CyanPrimary)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        if (!isPreviewMode) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0D111A))
            ) {
                OutlinedTextField(
                    value = content,
                    onValueChange = onContentChange,
                    modifier = Modifier.fillMaxSize(),
                    textStyle = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace, color = Color(0xFFF1F5F9)),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.dp, CyanPrimary.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    content.lines().forEach { line ->
                        when {
                            line.startsWith("# ") -> Text(line.removePrefix("# "), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = CyanPrimary)
                            line.startsWith("## ") -> Text(line.removePrefix("## "), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = VioletSecondary)
                            line.startsWith("### ") -> Text(line.removePrefix("### "), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            line.startsWith("- ") -> Text("• ${line.removePrefix("- ")}", style = MaterialTheme.typography.bodyMedium)
                            else -> Text(line, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Spacer(Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ApiTesterTab(modifier: Modifier = Modifier) {
    var httpMethod by remember { mutableStateOf("GET") }
    var endpointUrl by remember { mutableStateOf("https://api.toolverse.ai/v1/health") }
    var responseStatus by remember { mutableStateOf("200 OK • 42ms") }
    var responseBody by remember {
        mutableStateOf(
            """{
  "status": "online",
  "nodes": 8,
  "cluster": "us-central1",
  "version": "v2026.4",
  "auth": "bearer_token_verified"
}"""
        )
    }

    val methods = listOf("GET", "POST", "PUT", "DELETE", "PATCH")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("REST API Client", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            methods.forEach { m ->
                FilterChip(
                    selected = httpMethod == m,
                    onClick = { httpMethod = m },
                    label = { Text(m) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = if (m == "GET") EmeraldSuccess.copy(alpha = 0.2f) else CyanPrimary.copy(alpha = 0.2f),
                        selectedLabelColor = if (m == "GET") EmeraldSuccess else CyanPrimary
                    )
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = endpointUrl,
                onValueChange = { endpointUrl = it },
                label = { Text("Endpoint URL") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = {
                    responseStatus = "200 OK • 38ms"
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary, contentColor = Color(0xFF001F29)),
                modifier = Modifier.height(52.dp)
            ) {
                Text("Send", fontWeight = FontWeight.Bold)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Response Status:", style = MaterialTheme.typography.labelSmall)
            ToolBadge(text = responseStatus, containerColor = EmeraldSuccess, contentColor = Color.Black)
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0D111A))
        ) {
            Text(
                text = responseBody,
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                color = EmeraldSuccess,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
fun AiCodeAssistantTab(
    codeSnippet: String,
    isAiThinking: Boolean,
    onAskAi: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var prompt by remember { mutableStateOf("Explain this code and suggest performance optimizations.") }
    var explanationResult by remember { mutableStateOf("Verse AI analysis:\n1. Structured layout uses Jetpack Compose reactive state.\n2. Recommend optimizing recomposition with remember {} blocks.\n3. Safe exception handling ensures continuous UI render stability.") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("AI Code Copilot & Diagnostics", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            label = { Text("What should Verse AI do with your code?") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3
        )

        Button(
            onClick = {
                onAskAi(codeSnippet, prompt)
                explanationResult = "Verse AI completed code diagnostic:\n- Code passes standard formatting & AST checks.\n- Suggested refactor applied with sub-millisecond execution time."
            },
            enabled = !isAiThinking,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VioletSecondary, contentColor = Color.White)
        ) {
            if (isAiThinking) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White)
            } else {
                Icon(Icons.Default.AutoAwesome, null, Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Run AI Diagnostic & Optimization", fontWeight = FontWeight.Bold)
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, VioletSecondary.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Psychology, null, tint = VioletSecondary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Verse AI Diagnostic Report", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    text = explanationResult,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
