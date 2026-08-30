package com.example.chatapplication.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.chatapplication.ui.theme.ChatTheme
import kotlinx.coroutines.launch

/**
 * Premium Flagship Draggable Back Interaction
 *
 * Combines an effortless touch target with a tactile drag-to-pop gesture:
 * - Direct tap executes standard smooth back navigation.
 * - Horizontal swipe/drag progressively shifts the button and expands visual tactile feedback.
 * - Dragging past threshold triggers the back action with spring release.
 * - Releasing before threshold smoothly springs back to resting state.
 */
@Composable
fun DraggableBackButton(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onProgressChanged: ((Float) -> Unit)? = null
) {
    val colors = ChatTheme.colors
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val maxDragPx = with(density) { 90.dp.toPx() }
    val triggerThresholdPx = with(density) { 45.dp.toPx() }

    val dragOffset = remember { Animatable(0f) }
    var isDragging by remember { mutableStateOf(false) }

    val progress = (dragOffset.value / maxDragPx).coerceIn(0f, 1f)

    LaunchedEffect(progress) {
        onProgressChanged?.invoke(progress)
    }

    val containerWidth = (40 + (progress * 42)).dp
    val pillAlpha = (0.4f + progress * 0.6f)

    Box(
        modifier = modifier
            .height(40.dp)
            .width(containerWidth)
            .clip(CircleShape)
            .background(
                if (progress > 0.05f) colors.accentTint.copy(alpha = pillAlpha)
                else colors.secondarySurface.copy(alpha = 0.7f)
            )
            .border(
                width = 1.dp,
                color = if (progress > 0.35f) colors.accent.copy(alpha = 0.5f) else colors.border.copy(alpha = 0.6f),
                shape = CircleShape
            )
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = {
                        isDragging = true
                    },
                    onDragEnd = {
                        isDragging = false
                        scope.launch {
                            if (dragOffset.value >= triggerThresholdPx) {
                                // Trigger back navigation on deliberate gesture
                                dragOffset.animateTo(
                                    targetValue = maxDragPx,
                                    animationSpec = tween(120, easing = FastOutSlowInEasing)
                                )
                                onBack()
                            } else {
                                // Spring back naturally
                                dragOffset.animateTo(
                                    targetValue = 0f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                            }
                        }
                    },
                    onDragCancel = {
                        isDragging = false
                        scope.launch {
                            dragOffset.animateTo(0f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        if (dragAmount > 0 || dragOffset.value > 0) {
                            val newOffset = (dragOffset.value + dragAmount * 0.75f).coerceIn(0f, maxDragPx)
                            scope.launch {
                                dragOffset.snapTo(newOffset)
                            }
                        }
                    }
                )
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    scope.launch {
                        dragOffset.animateTo(12f, animationSpec = tween(60))
                        dragOffset.animateTo(0f, animationSpec = tween(60))
                        onBack()
                    }
                }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .graphicsLayer {
                    translationX = dragOffset.value * 0.4f
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = if (progress > 0.35f) colors.accent else colors.textPrimary,
                modifier = Modifier
                    .size(20.dp)
                    .graphicsLayer {
                        scaleX = 1f + (progress * 0.12f)
                        scaleY = 1f + (progress * 0.12f)
                    }
            )

            if (progress > 0.2f) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (progress >= 0.45f) "Release" else "Back",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (progress >= 0.45f) colors.accent else colors.textMuted,
                    modifier = Modifier.graphicsLayer {
                        alpha = ((progress - 0.2f) / 0.3f).coerceIn(0f, 1f)
                    }
                )
            }
        }
    }
}
