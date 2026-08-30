package com.example.chatapplication.ui.Screen.Main

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material.icons.rounded.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapplication.Data.Viewmodel.UserInfo
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.ui.components.AvatarView
import com.example.chatapplication.ui.components.DraggableBackButton
import com.example.chatapplication.ui.theme.ChatTheme
import com.example.chatapplication.ui.theme.LuxuryPalette
import com.example.chatapplication.ui.theme.ThemeController
import com.example.chatapplication.ui.theme.getColorsForPalette
import kotlinx.coroutines.launch

@Composable
fun profileScreen(
    nav: NavController,
    user: UserInfo,
    token: TokenManager,
    id: String
) {
    val colors = ChatTheme.colors
    val currentUser = user.userInfo.firstOrNull { it.id == id }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            if (bytes != null) {
                user.uploadImg(id, bytes)
            }
        }
    }

    Scaffold(
        containerColor = colors.background,
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 2.dp,
                        spotColor = colors.textPrimary.copy(alpha = 0.04f),
                        ambientColor = colors.textPrimary.copy(alpha = 0.02f)
                    ),
                color = colors.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.border.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DraggableBackButton(onBack = { nav.popBackStack() })

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "Profile & Settings",
                        style = MaterialTheme.typography.titleLarge,
                        color = colors.textPrimary
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Avatar Header
            Box(
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                AvatarView(
                    imageUrl = currentUser?.photo_url,
                    name = currentUser?.name ?: "User",
                    size = 110.dp
                )

                // Change photo action pill
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(colors.accent)
                        .border(2.dp, colors.surface, CircleShape)
                        .clickable {
                            imagePicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CameraAlt,
                        contentDescription = "Upload photo",
                        tint = colors.surface,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            Text(
                text = currentUser?.name ?: "Display Name",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium),
                color = colors.textPrimary
            )

            Text(
                text = "@${currentUser?.username ?: "username"}",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.accent,
                modifier = Modifier.padding(top = 2.dp, bottom = 24.dp)
            )

            // --- Multi-Theme Selection Studio ---
            ProfileSectionCard(title = "COLOR THEMES & AESTHETICS") {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Quiet Luxury Studio",
                                style = MaterialTheme.typography.titleMedium,
                                color = colors.textPrimary
                            )
                            Text(
                                text = "8 curated color palettes styled across every screen",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.textMuted
                            )
                        }

                        Icon(
                            imageVector = Icons.Rounded.Palette,
                            contentDescription = null,
                            tint = colors.accent,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Live Interactive Theme Cards
                    LuxuryPalette.entries.forEach { palette ->
                        val isSelected = ThemeController.currentPalette == palette
                        ThemePaletteCard(
                            palette = palette,
                            isSelected = isSelected,
                            onSelect = { ThemeController.currentPalette = palette }
                        )
                        if (palette != LuxuryPalette.entries.last()) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Account Details Section
            ProfileSectionCard(title = "ACCOUNT DETAILS") {
                ProfileInfoRow(
                    icon = Icons.Rounded.PersonOutline,
                    label = "Full Name",
                    value = currentUser?.name ?: "Not set"
                )
                HorizontalDivider(modifier = Modifier.padding(start = 52.dp), thickness = 0.5.dp, color = colors.subtleDivider)
                ProfileInfoRow(
                    icon = Icons.Rounded.AlternateEmail,
                    label = "Username",
                    value = "@${currentUser?.username ?: "not_set"}"
                )
                HorizontalDivider(modifier = Modifier.padding(start = 52.dp), thickness = 0.5.dp, color = colors.subtleDivider)
                ProfileInfoRow(
                    icon = Icons.Rounded.MailOutline,
                    label = "Email",
                    value = currentUser?.email ?: "Not set"
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Settings Preferences Section
            ProfileSectionCard(title = "PREFERENCES") {
                ProfileSettingRow(
                    icon = Icons.Rounded.NotificationsNone,
                    label = "Notifications & Sounds",
                    onClick = {}
                )
                HorizontalDivider(modifier = Modifier.padding(start = 52.dp), thickness = 0.5.dp, color = colors.subtleDivider)
                ProfileSettingRow(
                    icon = Icons.Rounded.Lock,
                    label = "Privacy & Security",
                    onClick = {}
                )
                HorizontalDivider(modifier = Modifier.padding(start = 52.dp), thickness = 0.5.dp, color = colors.subtleDivider)
                ProfileSettingRow(
                    icon = Icons.Rounded.Storage,
                    label = "Data & Storage",
                    onClick = {}
                )
                HorizontalDivider(modifier = Modifier.padding(start = 52.dp), thickness = 0.5.dp, color = colors.subtleDivider)
                ProfileSettingRow(
                    icon = Icons.Rounded.Info,
                    label = "About YOKOSU",
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Logout Action Button
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, colors.border, RoundedCornerShape(16.dp))
                    .clickable { showLogoutDialog = true },
                color = colors.surface,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Logout,
                        contentDescription = "Sign Out",
                        tint = colors.danger,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sign Out",
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.danger
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Logout Confirmation Dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = {
                    Text(
                        text = "Sign Out",
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.textPrimary
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to sign out of your account on this device?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textMuted
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            scope.launch {
                                token.clearTokens()
                                nav.navigate("SignIn") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "Sign Out",
                            style = MaterialTheme.typography.labelLarge,
                            color = colors.danger
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text(
                            text = "Cancel",
                            style = MaterialTheme.typography.labelLarge,
                            color = colors.textPrimary
                        )
                    }
                },
                containerColor = colors.surface,
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

/**
 * Interactive Theme Palette Card with Swatches and Mini Bubble Preview
 */
@Composable
private fun ThemePaletteCard(
    palette: LuxuryPalette,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val currentColors = ChatTheme.colors
    val pColors = getColorsForPalette(palette)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) currentColors.accent else currentColors.border.copy(alpha = 0.7f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onSelect),
        color = if (isSelected) currentColors.accentTint.copy(alpha = 0.35f) else currentColors.surface,
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Theme Swatch Pair
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(palette.secondaryPreview)
                    .border(1.dp, currentColors.border, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(palette.previewColor)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = palette.title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = currentColors.textPrimary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(palette.secondaryPreview)
                            .padding(horizontal = 6.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = palette.subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = currentColors.textPrimary,
                            fontSize = 10.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = palette.description,
                    style = MaterialTheme.typography.labelSmall,
                    color = currentColors.textMuted,
                    maxLines = 1
                )
            }

            // Selection Checkmark
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(currentColors.accent),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "Active Theme",
                        tint = currentColors.surface,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    val colors = ChatTheme.colors

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            ),
            color = colors.textMuted,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, colors.border, RoundedCornerShape(18.dp)),
            color = colors.surface,
            shape = RoundedCornerShape(18.dp)
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    val colors = ChatTheme.colors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(colors.secondarySurface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.textMuted,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = colors.textMuted
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = colors.textPrimary
            )
        }
    }
}

@Composable
private fun ProfileSettingRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    val colors = ChatTheme.colors

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(colors.secondarySurface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.textMuted,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = colors.textPrimary,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = colors.textMuted.copy(alpha = 0.6f),
            modifier = Modifier.size(20.dp)
        )
    }
}

/**
 * Legacy compatibility wrapper for existing call signatures if any.
 */
@Composable
fun tile(content: String?) {
    val colors = ChatTheme.colors

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp)),
        color = colors.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, colors.border)
    ) {
        Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(text = content ?: "", style = MaterialTheme.typography.bodyLarge, color = colors.textPrimary)
        }
    }
}