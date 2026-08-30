package com.example.chatapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.chatapplication.ui.theme.ChatTheme

/**
 * Flagship Date Divider Chip
 */
@Composable
fun DateHeaderChip(
    dateText: String,
    modifier: Modifier = Modifier
) {
    val colors = ChatTheme.colors

    Box(
        modifier = modifier.padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(colors.secondarySurface.copy(alpha = 0.9f))
                .padding(horizontal = 12.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dateText,
                style = MaterialTheme.typography.labelSmall,
                color = colors.textMuted
            )
        }
    }
}
