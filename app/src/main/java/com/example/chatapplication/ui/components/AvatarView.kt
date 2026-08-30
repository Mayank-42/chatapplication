package com.example.chatapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.chatapplication.ui.theme.ChatTheme

/**
 * Flagship Luxury Avatar Component
 *
 * Displays remote image, initials monogram, or group icon with subtle borders and presence dot.
 */
@Composable
fun AvatarView(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    name: String = "",
    size: Dp = 48.dp,
    isOnline: Boolean = false,
    showOnlineIndicator: Boolean = false,
    isGroup: Boolean = false
) {
    val colors = ChatTheme.colors
    val initial = name.trim().takeIf { it.isNotEmpty() }?.first()?.uppercaseChar()?.toString() ?: ""

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Avatar circular container
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(if (isGroup) colors.secondarySurface else colors.accentTint)
                .border(1.dp, colors.border.copy(alpha = 0.8f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (!imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
                )
            } else if (isGroup) {
                Icon(
                    imageVector = Icons.Rounded.Groups,
                    contentDescription = "Group",
                    tint = colors.accentDark,
                    modifier = Modifier.size((size.value * 0.45f).dp)
                )
            } else if (initial.isNotEmpty()) {
                Text(
                    text = initial,
                    color = colors.textPrimary,
                    fontSize = (size.value * 0.4f).sp,
                    fontWeight = FontWeight.Medium
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = "Person",
                    tint = colors.accentDark,
                    modifier = Modifier.size((size.value * 0.45f).dp)
                )
            }
        }

        // Online presence indicator dot
        if (showOnlineIndicator && isOnline) {
            val dotSize = (size.value * 0.26f).coerceIn(9f, 16f).dp
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(colors.surface)
                    .border(2.dp, colors.surface, CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .clip(CircleShape)
                        .background(colors.statusSuccess)
                )
            }
        }
    }
}
