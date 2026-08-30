package com.example.chatapplication.ui.Screen.Main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatapplication.Data.Viewmodel.UserInfo
import com.example.chatapplication.Data.Viewmodel.databaseVM
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.ui.components.AvatarView
import com.example.chatapplication.ui.components.EmptyStateView
import com.example.chatapplication.ui.theme.ChatTheme
import com.example.chatapplication.ui.theme.ThemeController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navControl: NavController,
    tokenManager: TokenManager,
    userinfoo: UserInfo,
    onLoginSuccess: () -> Unit,
    converatation: databaseVM? = null
) {
    val colors = ChatTheme.colors
    val scope = rememberCoroutineScope()
    var currentUserId by rememberSaveable { mutableStateOf("") }
    var selectedTab by rememberSaveable { mutableStateOf("messages") }

    LaunchedEffect(Unit) {
        userinfoo.getinfo()
        currentUserId = tokenManager.getUserId() ?: ""
    }

    val currentUser = userinfoo.userInfo.firstOrNull { it.id == currentUserId }

    Scaffold(
        containerColor = colors.background,
        bottomBar = {
            // Flagship Minimalist Bottom Dock
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        spotColor = colors.textPrimary.copy(alpha = 0.06f),
                        ambientColor = colors.textPrimary.copy(alpha = 0.03f)
                    ),
                color = colors.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.border.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Messages Dock Item
                    BottomDockItem(
                        icon = Icons.Rounded.ChatBubbleOutline,
                        activeIcon = Icons.Rounded.ChatBubble,
                        label = "Chats",
                        isSelected = selectedTab == "messages",
                        onClick = { selectedTab = "messages" }
                    )

                    // Groups Dock Item
                    BottomDockItem(
                        icon = Icons.Rounded.Groups,
                        activeIcon = Icons.Rounded.Groups,
                        label = "Groups",
                        isSelected = false,
                        onClick = { navControl.navigate("GroupPage") }
                    )

                    // Profile Dock Item
                    BottomDockItem(
                        icon = Icons.Rounded.Person,
                        activeIcon = Icons.Rounded.Person,
                        label = "Profile",
                        isSelected = false,
                        onClick = {
                            if (currentUserId.isNotBlank()) {
                                navControl.navigate("profileScreen/$currentUserId")
                            }
                        }
                    )

                    // Logout Dock Item
                    BottomDockItem(
                        icon = Icons.AutoMirrored.Rounded.Logout,
                        activeIcon = Icons.AutoMirrored.Rounded.Logout,
                        label = "Logout",
                        isSelected = false,
                        onClick = {
                            scope.launch {
                                tokenManager.clearTokens()
                                onLoginSuccess()
                            }
                        }
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
                .statusBarsPadding()
        ) {
            // Top Section: Large Heading + Theme Switcher & Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 20.dp, top = 16.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Messages",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Light,
                            fontSize = 34.sp
                        ),
                        color = colors.textPrimary
                    )
                    Text(
                        text = "${userinfoo.userInfo.size} active contacts • ${ThemeController.currentPalette.title}",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.textMuted
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Quick Theme Cycling Pill
                    Box(
                        modifier = Modifier
                            .height(36.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(colors.accentTint)
                            .border(1.dp, colors.accent.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                            .clickable { ThemeController.nextTheme() }
                            .padding(horizontal = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(colors.accent)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Rounded.ColorLens,
                                contentDescription = "Cycle Theme",
                                tint = colors.accent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // New Chat Action
                    IconButton(
                        onClick = { navControl.navigate("GropChatSearch") },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(colors.secondarySurface.copy(alpha = 0.7f))
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.EditNote,
                            contentDescription = "New chat",
                            tint = colors.textPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Current User Profile Avatar
                    Box(
                        modifier = Modifier
                            .clickable {
                                if (currentUserId.isNotBlank()) {
                                    navControl.navigate("profileScreen/$currentUserId")
                                }
                            }
                    ) {
                        AvatarView(
                            imageUrl = currentUser?.photo_url,
                            name = currentUser?.name ?: "You",
                            size = 38.dp,
                            showOnlineIndicator = true,
                            isOnline = true
                        )
                    }
                }
            }

            // Search Trigger Bar (Minimalist soft surface)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .height(46.dp)
                    .clip(RoundedCornerShape(23.dp))
                    .border(1.dp, colors.border.copy(alpha = 0.8f), RoundedCornerShape(23.dp))
                    .clickable { navControl.navigate("SearchBarPage") },
                color = colors.surface,
                shape = RoundedCornerShape(23.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search",
                        tint = colors.textMuted,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Search users and conversations...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textMuted.copy(alpha = 0.75f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Conversation List Section
            val otherUsers = userinfoo.userInfo.filter { it.id != currentUserId }

            if (otherUsers.isEmpty()) {
                EmptyStateView(
                    icon = Icons.Rounded.ChatBubbleOutline,
                    title = "No conversations yet",
                    subtitle = "Search for a colleague or start a new group conversation to begin messaging.",
                    actionText = "Find Colleagues",
                    onActionClick = { navControl.navigate("SearchBarPage") },
                    modifier = Modifier.weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(otherUsers, key = { it.id }) { ele ->
                        ConversationRowItem(
                            name = ele.name,
                            username = ele.username,
                            imageUrl = ele.photo_url,
                            timestamp = ele.created_at ?: "",
                            isOnline = true,
                            onClick = {
                                userinfoo.openConversation(otherUserId = ele.id) { conversationId ->
                                    navControl.navigate("ChatScreen/$conversationId")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Flagship Clean Conversation Row (No floating cards)
 */
@Composable
private fun ConversationRowItem(
    name: String,
    username: String,
    imageUrl: String?,
    timestamp: String,
    isOnline: Boolean,
    onClick: () -> Unit
) {
    val colors = ChatTheme.colors

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with Presence
                AvatarView(
                    imageUrl = imageUrl,
                    name = name,
                    size = 52.dp,
                    isOnline = isOnline,
                    showOnlineIndicator = true
                )

                Spacer(modifier = Modifier.width(14.dp))

                // Content Column
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium,
                            color = colors.textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = formatTimestampPreview(timestamp),
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "@$username • Tap to open conversation",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.textMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Subtle row divider with left inset matching avatar
            HorizontalDivider(
                modifier = Modifier.padding(start = 86.dp, end = 20.dp),
                thickness = 0.5.dp,
                color = colors.subtleDivider
            )
        }
    }
}

/**
 * Clean Bottom Dock Item
 */
@Composable
private fun BottomDockItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    activeIcon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = ChatTheme.colors

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .height(32.dp)
                .width(48.dp)
                .clip(CircleShape)
                .background(if (isSelected) colors.accentTint else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isSelected) activeIcon else icon,
                contentDescription = label,
                tint = if (isSelected) colors.accent else colors.textMuted,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) colors.textPrimary else colors.textMuted,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

private fun formatTimestampPreview(raw: String): String {
    if (raw.isBlank()) return "Recently"
    return try {
        if (raw.contains("T")) {
            val datePart = raw.substringBefore("T")
            datePart.takeLast(5)
        } else {
            raw.take(10)
        }
    } catch (e: Exception) {
        "Recently"
    }
}
