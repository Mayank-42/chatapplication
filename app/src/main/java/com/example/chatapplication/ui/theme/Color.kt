package com.example.chatapplication.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Quiet Luxury Theme Color Tokens
 *
 * All palettes adhere strictly to:
 * ~85% Neutral (Pearl, Soft Stone, Surface)
 * ~10% Typography (Graphite, Muted Slate)
 * ~5% Accent (Soft Lavender, Nordic Sage, Cashmere, Slate, Rose, Olive, Terracotta, Obsidian)
 *
 * NO Loud Orange. NO Neon Colors. NO Clutter.
 */

// --- Base Tokens ---
val PearlBackground = Color(0xFFF7F7F5)
val WhiteSurface = Color(0xFFFFFFFF)
val SoftStone = Color(0xFFEEEDEA)
val GraphiteText = Color(0xFF29282D)
val MutedText = Color(0xFF77757D)
val BorderColor = Color(0xFFE3E2E5)
val SubtleDivider = Color(0xFFEFEFEB)

val SoftLavender = Color(0xFF8D88C7)
val LavenderTint = Color(0xFFEAE8F5)
val LavenderDark = Color(0xFF7570AE)

val SoftSage = Color(0xFF88A99A)
val SoftSageTint = Color(0xFFE5EEEA)

val BubbleIncoming = Color(0xFFEEEDEA)
val BubbleIncomingText = Color(0xFF29282D)
val BubbleOutgoing = Color(0xFFEAE8F5)
val BubbleOutgoingText = Color(0xFF29282D)

val SoftDanger = Color(0xFFD46868)
val SoftDangerTint = Color(0xFFFBEAEA)

// --- Harmonic Group Sender Colors (Derived for Group Chats) ---
val GroupSenderColors = listOf(
    Color(0xFF7B72A9),
    Color(0xFF5B8574),
    Color(0xFF8C7365),
    Color(0xFF537385),
    Color(0xFF916574),
    Color(0xFF6B7E63),
    Color(0xFF8E6A62)
)

// Backward compatibility aliases
val Purple80 = SoftLavender
val PurpleGrey80 = LavenderTint
val Pink80 = SoftSage
val Purple40 = SoftLavender
val PurpleGrey40 = SoftStone
val Pink40 = SoftSage