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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.components.ToolBadge
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.GoldPro
import com.example.ui.theme.VioletSecondary

@Composable
fun AuthScreen(
    userProfile: UserProfile,
    onUpgradeToPro: () -> Unit,
    onDowngradeToFree: () -> Unit,
    onShowNotification: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isSignUpTab by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf(userProfile.email) }
    var password by remember { mutableStateOf("••••••••••••") }
    var name by remember { mutableStateOf(userProfile.name) }

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
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("auth_back_btn")) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Account & Subscription",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(CyanPrimary, VioletSecondary))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(36.dp))
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(userProfile.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(userProfile.email, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(8.dp))
                    ToolBadge(
                        text = "${userProfile.planType.uppercase()} SUBSCRIBER",
                        containerColor = if (userProfile.planType == "Pro") GoldPro else CyanPrimary.copy(alpha = 0.2f),
                        contentColor = if (userProfile.planType == "Pro") Color.Black else CyanPrimary
                    )
                }
            }

            // Subscription Tiers
            Text("Subscription Plan Comparison", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            // Free Tier
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        if (userProfile.planType == "Free") 2.dp else 1.dp,
                        if (userProfile.planType == "Free") CyanPrimary else MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(14.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Free Tier", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        if (userProfile.planType == "Free") ToolBadge(text = "ACTIVE", containerColor = CyanPrimary, contentColor = Color(0xFF001F29))
                    }
                    Text("$0 / month", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = CyanPrimary)
                    Spacer(Modifier.height(6.dp))
                    Text("• 50 AI generations / month\n• 500 MB cloud project storage\n• 25+ Standard tools access", style = MaterialTheme.typography.bodySmall, lineHeight = 18.sp)
                    if (userProfile.planType == "Pro") {
                        Spacer(Modifier.height(10.dp))
                        OutlinedButton(onClick = onDowngradeToFree, modifier = Modifier.fillMaxWidth()) {
                            Text("Switch to Free")
                        }
                    }
                }
            }

            // Pro Tier
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        if (userProfile.planType == "Pro") 2.dp else 1.dp,
                        if (userProfile.planType == "Pro") GoldPro else MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(14.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("ToolVerse Pro", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GoldPro)
                        if (userProfile.planType == "Pro") ToolBadge(text = "ACTIVE", containerColor = GoldPro, contentColor = Color.Black)
                    }
                    Text("$19 / month", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = GoldPro)
                    Spacer(Modifier.height(6.dp))
                    Text("• 500 AI credits / month\n• 5 GB high-speed cloud storage\n• 4K Video export & RAW photo grading\n• Custom domain website hosting\n• Full 35+ tools unrestricted", style = MaterialTheme.typography.bodySmall, lineHeight = 18.sp)

                    if (userProfile.planType != "Pro") {
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = onUpgradeToPro,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPro, contentColor = Color.Black)
                        ) {
                            Text("Upgrade to Pro Tier", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
