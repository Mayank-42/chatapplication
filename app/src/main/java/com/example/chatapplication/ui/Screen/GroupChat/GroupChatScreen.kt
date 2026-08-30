package com.example.chatapplication.ui.Screen.GroupChat

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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapplication.Data.Viewmodel.GroupChatVM
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
import com.example.chatapplication.ui.theme.GroupSenderColors
import kotlin.math.abs

@Composable
fun GroupChatScreen(
    navControl: NavController,
    viewMode: databaseVM,
    userId: String?,
    msg: MsgVM,
    tokenManager: TokenManager,
    save: GroupChatVM
) {
    val colors = ChatTheme.colors
    var currentUserId by rememberSaveable { mutableStateOf("") }
    var textMessage by rememberSaveable { mutableStateOf("") }
    var gestureProgress by remember { mutableFloatStateOf(0f) }
    var showMoreMenu by remember { mutableStateOf(false) }
    var selectedMessageForDelete by remember { mutableStateOf<MessageInfo?>(null) }

    LaunchedEffect(Unit) {
        currentUserId = tokenManager.getUserId() ?: ""
    }

    val messagesList by viewMode.getallValue.collectAsState(initial = emptyList())

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
                    DraggableBackButton(
                        onBack = { navControl.popBackStack() },
                        onProgressChanged = { gestureProgress = it }
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    AvatarView(
                        name = "Group",
                        size = 40.dp,
                        isGroup = true
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Group Chat",
                            style = MaterialTheme.typography.titleMedium,
                            color = colors.textPrimary
                        )
                        Text(
                            text = "${save.selectedUserId.size + 1} members",
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textMuted
                        )
                    }

                    IconButton(onClick = {}, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Rounded.Call,
                            contentDescription = "Group Call",
                            tint = colors.textPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(onClick = {}, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Rounded.Videocam,
                            contentDescription = "Group Video",
                            tint = colors.textPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Box {
                        IconButton(onClick = { showMoreMenu = true }, modifier = Modifier.size(36.dp)) {
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
                                text = { Text("Group info", style = MaterialTheme.typography.bodyMedium, color = colors.textPrimary) },
                                onClick = { showMoreMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Search messages", style = MaterialTheme.typography.bodyMedium, color = colors.textPrimary) },
                                onClick = { showMoreMenu = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Leave group", style = MaterialTheme.typography.bodyMedium, color = colors.danger) },
                                onClick = { showMoreMenu = false }
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            LuxuryComposer(
                value = textMessage,
                onValueChange = { textMessage = it },
                placeholder = "Message group...",
                onSend = {
                    if (textMessage.isNotBlank()) {
                        val toSend = textMessage
                        textMessage = ""
                        msg.storeMsg(userId ?: "", toSend)
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
                    translationX = gestureProgress * 40f
                    alpha = 1f - (gestureProgress * 0.15f)
                }
        ) {
            if (messagesList.isEmpty()) {
                EmptyStateView(
                    icon = Icons.Rounded.Call,
                    title = "Group conversation started",
                    subtitle = "All members can read and send messages in this group.",
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
                        val senderIdx = abs(ele.sender_Id.hashCode()) % GroupSenderColors.size
                        val senderColor = GroupSenderColors[senderIdx]

                        ChatBubble(
                            message = ele.message,
                            timestamp = ele.date,
                            isOutgoing = isOutgoing,
                            senderName = if (!isOutgoing) ele.sender_Id.take(8) else null,
                            senderColor = senderColor,
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

        // Delete confirmation dialog
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