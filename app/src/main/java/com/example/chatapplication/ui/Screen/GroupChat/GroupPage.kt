package com.example.chatapplication.ui.Screen.GroupChat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.GroupAdd
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapplication.Data.Viewmodel.GroupChatVM
import com.example.chatapplication.ui.components.AvatarView
import com.example.chatapplication.ui.components.DraggableBackButton
import com.example.chatapplication.ui.components.EmptyStateView
import com.example.chatapplication.ui.theme.ChatTheme

@Composable
fun GroupPage(nav: NavController, save: GroupChatVM) {
    val colors = ChatTheme.colors
    val groups by save.gettingGroupinfo.collectAsState(initial = emptyList())

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
                        text = "Group Conversations",
                        style = MaterialTheme.typography.titleLarge,
                        color = colors.textPrimary,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = { nav.navigate("GropChatSearch") },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(colors.accentTint)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "New Group",
                            tint = colors.accent,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { nav.navigate("GropChatSearch") },
                containerColor = colors.accent,
                contentColor = colors.surface,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp),
                modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.GroupAdd,
                    contentDescription = "Create Group",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(paddingValues)
        ) {
            if (groups.isEmpty()) {
                EmptyStateView(
                    icon = Icons.Rounded.Groups,
                    title = "No group conversations",
                    subtitle = "Create a workspace group with your colleagues to collaborate effortlessly.",
                    actionText = "Create New Group",
                    onActionClick = { nav.navigate("GropChatSearch") },
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(groups, key = { it.GroupId }) { ele ->
                        GroupRowItem(
                            groupName = ele.GropName,
                            bio = ele.bio,
                            onClick = { nav.navigate("GroupChatScreen") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GroupRowItem(
    groupName: String,
    bio: String,
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
                AvatarView(
                    name = groupName,
                    size = 52.dp,
                    isGroup = true
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = groupName,
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = if (bio.isNotBlank()) bio else "Workspace Group",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(start = 86.dp, end = 20.dp),
                thickness = 0.5.dp,
                color = colors.subtleDivider
            )
        }
    }
}

/**
 * Legacy compatibility wrapper for existing call signatures if any.
 */
@Composable
fun GropChatTile(Gname: String, nav: NavController) {
    GroupRowItem(groupName = Gname, bio = "Workspace Group", onClick = { nav.navigate("GroupChatScreen") })
}
