package com.example.chatapplication.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatapplication.ui.theme.ChatTheme

/**
 * Flagship Luxury Chat Bubble
 *
 * Implements subtle, high-end messaging bubbles:
 * - Incoming: Soft Stone neutral background with Graphite text
 * - Outgoing: Subtle Accent Tint background with Graphite text
 * - Tailored organic corner radii (18dp with 4dp tail)
 * - Clean timestamps & status checkmarks
 * - Sender attribution with harmonic colors for group chats
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatBubble(
    modifier: Modifier = Modifier,
    message: String,
    timestamp: String,
    isOutgoing: Boolean,
    senderName: String? = null,
    senderColor: Color? = null,
    onLongClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    val colors = ChatTheme.colors

    val bubbleShape = if (isOutgoing) {
        RoundedCornerShape(
            topStart = 18.dp,
            topEnd = 18.dp,
            bottomStart = 18.dp,
            bottomEnd = 4.dp
        )
    } else {
        RoundedCornerShape(
            topStart = 18.dp,
            topEnd = 18.dp,
            bottomStart = 4.dp,
            bottomEnd = 18.dp
        )
    }

    val bubbleBg = if (isOutgoing) colors.bubbleOutgoingBg else colors.bubbleIncomingBg
    val textColor = if (isOutgoing) colors.bubbleOutgoingText else colors.bubbleIncomingText

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 3.dp),
        horizontalArrangement = if (isOutgoing) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier.widthIn(max = 300.dp),
            horizontalAlignment = if (isOutgoing) Alignment.End else Alignment.Start
        ) {
            // Group Sender Name label if incoming in group
            if (!isOutgoing && !senderName.isNullOrBlank()) {
                Text(
                    text = senderName,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = senderColor ?: colors.accentDark,
                    modifier = Modifier.padding(start = 8.dp, bottom = 3.dp)
                )
            }

            Surface(
                shape = bubbleShape,
                color = bubbleBg,
                modifier = Modifier
                    .clip(bubbleShape)
                    .combinedClickable(
                        onClick = onClick,
                        onLongClick = onLongClick
                    )
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp)
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor,
                        lineHeight = 21.sp
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatTimestamp(timestamp),
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.textMuted.copy(alpha = 0.85f),
                            fontSize = 10.sp
                        )

                        if (isOutgoing) {
                            Spacer(modifier = Modifier.width(3.dp))
                            Icon(
                                imageVector = Icons.Rounded.DoneAll,
                                contentDescription = "Delivered",
                                tint = colors.accent,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Formats full timestamp / date string into a clean HH:mm presentation.
 */
private fun formatTimestamp(raw: String): String {
    if (raw.isBlank()) return ""
    return try {
        if (raw.contains("T")) {
            val timePart = raw.substringAfter("T").substringBefore(".")
            val parts = timePart.split(":")
            if (parts.size >= 2) "${parts[0]}:${parts[1]}" else timePart
        } else if (raw.contains(" ")) {
            val parts = raw.split(" ")
            parts.lastOrNull()?.take(5) ?: raw
        } else {
            raw.takeLast(5)
        }
    } catch (e: Exception) {
        raw.takeLast(5)
    }
}
