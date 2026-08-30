package com.example.chatapplication.ui.Screen.Main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapplication.Data.Viewmodel.MsgVM
import com.example.chatapplication.Data.Viewmodel.databaseVM
import com.example.chatapplication.Data.local.TokenManager
import com.example.chatapplication.Data.local.tables.MessageInfo
import com.example.chatapplication.ui.components.AvatarView
import com.example.chatapplication.ui.components.ChatBubble
import com.example.chatapplication.ui.components.DateHeaderChip
import com.example.chatapplication.ui.components.DraggableBackButton
import com.example.chatapplication.ui.components.EmptyStateView
import com.example.chatapplication.ui.components.LuxuryComposer
import com.example.chatapplication.ui.theme.ChatTheme

@Composable
fun chatScreen(
    navControl: NavController,
    viewMode: databaseVM,
    conversationId: String,
    msg: MsgVM,
    tokenManager: TokenManager
) {
    val colors = ChatTheme.colors
    var currentUserId by rememberSaveable { mutableStateOf("") }
    var textMessage by rememberSaveable { mutableStateOf("") }
    var gestureProgress by remember { mutableFloatStateOf(0f) }
    var showMoreMenu by remember { mutableStateOf(false) }
    var selectedMessageForDelete by remember { mutableStateOf<MessageInfo?>(null) }

    LaunchedEffect(conversationId) {
        try {
            currentUserId = tokenManager.getUserId() ?: ""
            msg.startRealtime(conversationId)
            msg.insertingLocaly(conversationId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val messagesList by viewMode.getConversation(conversationId).collectAsState(initial = emptyList())

    Scaffold(
        containerColor = colors.background,
        topBar = {
            // Flagship Clean Header
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 3.dp,
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
                    // Tactile Draggable Back Control
                    DraggableBackButton(
                        onBack = { navControl.popBackStack() },
                        onProgressChanged = { gestureProgress = it }
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    // Contact Avatar
                    AvatarView(
                        name = "Chat",
                        size = 40.dp,
                        showOnlineIndicator = true,
                        isOnline = true
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // Contact Name & Presence Info
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Conversation",
                            style = MaterialTheme.typography.titleMedium,
                            color = colors.textPrimary
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(colors.statusSuccess)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Active now",
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.statusSuccess
                            )
                        }
                    }

                    // Header Action Icons
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Call,
                                contentDescription = "Voice Call",
                                tint = colors.textPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Videocam,
                                contentDescription = "Video Call",
                                tint = colors.textPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Box {
                            IconButton(
                                onClick = { showMoreMenu = true },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.MoreVert,
                                    contentDescription = "More",
                                    tint = colors.textPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = showMoreMenu,
                                onDismissRequest = { showMoreMenu = false },
                                modifier = Modifier.background(colors.surface)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Search in conversation", style = MaterialTheme.typography.bodyMedium, color = colors.textPrimary) },
                                    onClick = { showMoreMenu = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Media & files", style = MaterialTheme.typography.bodyMedium, color = colors.textPrimary) },
                                    onClick = { showMoreMenu = false }
                                )
                                DropdownMenuItem(
                                    text = { Text("Clear chat", style = MaterialTheme.typography.bodyMedium, color = colors.danger) },
                                    onClick = { showMoreMenu = false }
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            // Floating Luxury Message Composer
            LuxuryComposer(
                value = textMessage,
                onValueChange = { textMessage = it },
                placeholder = "Write a message...",
                onSend = {
                    if (textMessage.isNotBlank()) {
                        val toSend = textMessage
                        textMessage = ""
                        msg.storeMsg(conversationId, toSend)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(paddingValues)
                .graphicsLayer {
                    // Smooth content shift on back drag gesture
                    translationX = gestureProgress * 40f
                    alpha = 1f - (gestureProgress * 0.15f)
                }
        ) {
            if (messagesList.isEmpty()) {
                EmptyStateView(
                    icon = Icons.Rounded.Call,
                    title = "End-to-end encrypted",
                    subtitle = "Messages and calls are secured with private realtime delivery. Say hello to start the conversation.",
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 8.dp),
                    reverseLayout = true
                ) {
                    items(messagesList.reversed(), key = { it.id }) { ele ->
                        val isOutgoing = ele.sender_Id == currentUserId

                        ChatBubble(
                            message = ele.message,
                            timestamp = ele.date,
                            isOutgoing = isOutgoing,
                            onLongClick = {
                                selectedMessageForDelete = ele
                            }
                        )
                    }

                    item {
                        DateHeaderChip(
                            dateText = "Today",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Delete Message Confirmation Dialog
        selectedMessageForDelete?.let { msgToDelete ->
            AlertDialog(
                onDismissRequest = { selectedMessageForDelete = null },
                title = {
                    Text(
                        text = "Delete Message",
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.textPrimary
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to delete this message locally?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textMuted
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewMode.delete(msgToDelete)
                            selectedMessageForDelete = null
                        }
                    ) {
                        Text(
                            text = "Delete",
                            style = MaterialTheme.typography.labelLarge,
                            color = colors.danger
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { selectedMessageForDelete = null }) {
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
