package com.example.chatapplication.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

/**
 * Stunning Dark Mode & Luxury Theme Suite
 */
enum class LuxuryPalette(
    val title: String,
    val subtitle: String,
    val description: String,
    val previewColor: Color,
    val secondaryPreview: Color,
    val isDark: Boolean = true
) {
    MidnightIris(
        title = "Midnight Iris",
        subtitle = "Sleek Dark Mode",
        description = "Deep noir titanium with electric violet iris accents",
        previewColor = Color(0xFF8B5CF6),
        secondaryPreview = Color(0xFF261E3B),
        isDark = true
    ),
    CyberEmerald(
        title = "Cyber Emerald",
        subtitle = "Dark Botanical",
        description = "Dark onyx surfaces with vibrant emerald mint accents",
        previewColor = Color(0xFF10B981),
        secondaryPreview = Color(0xFF132E27),
        isDark = true
    ),
    ElectricCyan(
        title = "Electric Cyan",
        subtitle = "Sapphire Dark",
        description = "Midnight ocean navy with vivid sapphire cyan accents",
        previewColor = Color(0xFF06B6D4),
        secondaryPreview = Color(0xFF143542),
        isDark = true
    ),
    RoyalAmethyst(
        title = "Royal Amethyst",
        subtitle = "Velvet Dark",
        description = "Rich velvet dark violet with neon amethyst accents",
        previewColor = Color(0xFFA855F7),
        secondaryPreview = Color(0xFF311C47),
        isDark = true
    ),
    CrimsonRose(
        title = "Crimson Coral",
        subtitle = "Sunset Dark",
        description = "Warm espresso charcoal with vivid crimson rose accents",
        previewColor = Color(0xFFF43F5E),
        secondaryPreview = Color(0xFF3B1C23),
        isDark = true
    ),
    PearlMinimal(
        title = "Pearl Minimal",
        subtitle = "Soft Light Mode",
        description = "Clean pearl white with soft lavender minimal accents",
        previewColor = Color(0xFF8D88C7),
        secondaryPreview = Color(0xFFEAE8F5),
        isDark = false
    )
}

/**
 * Semantic Color Tokens for the entire app
 */
@Immutable
data class ChatColors(
    val background: Color,
    val surface: Color,
    val secondarySurface: Color,
    val textPrimary: Color,
    val textMuted: Color,
    val border: Color,
    val subtleDivider: Color,
    val accent: Color,
    val accentTint: Color,
    val accentDark: Color,
    val statusSuccess: Color,
    val statusSuccessTint: Color,
    val bubbleIncomingBg: Color,
    val bubbleIncomingText: Color,
    val bubbleOutgoingBg: Color,
    val bubbleOutgoingText: Color,
    val danger: Color,
    val dangerTint: Color,
    val isDark: Boolean = true
)

// 1. Midnight Iris (Default Dark Mode)
private val MidnightIrisColors = ChatColors(
    background = Color(0xFF0F0F12),
    surface = Color(0xFF18181C),
    secondarySurface = Color(0xFF222228),
    textPrimary = Color(0xFFF9FAFB),
    textMuted = Color(0xFF9CA3AF),
    border = Color(0xFF2E2E38),
    subtleDivider = Color(0xFF25252E),
    accent = Color(0xFF8B5CF6),
    accentTint = Color(0xFF261E3B),
    accentDark = Color(0xFFA78BFA),
    statusSuccess = Color(0xFF10B981),
    statusSuccessTint = Color(0xFF132E27),
    bubbleIncomingBg = Color(0xFF222228),
    bubbleIncomingText = Color(0xFFF9FAFB),
    bubbleOutgoingBg = Color(0xFF3B2A63),
    bubbleOutgoingText = Color(0xFFF9FAFB),
    danger = Color(0xFFEF4444),
    dangerTint = Color(0xFF381C1C),
    isDark = true
)

// 2. Cyber Emerald (Dark Mode)
private val CyberEmeraldColors = ChatColors(
    background = Color(0xFF0D1117),
    surface = Color(0xFF161B22),
    secondarySurface = Color(0xFF21262D),
    textPrimary = Color(0xFFF0F6FC),
    textMuted = Color(0xFF8B949E),
    border = Color(0xFF30363D),
    subtleDivider = Color(0xFF21262D),
    accent = Color(0xFF10B981),
    accentTint = Color(0xFF132E27),
    accentDark = Color(0xFF34D399),
    statusSuccess = Color(0xFF10B981),
    statusSuccessTint = Color(0xFF132E27),
    bubbleIncomingBg = Color(0xFF21262D),
    bubbleIncomingText = Color(0xFFF0F6FC),
    bubbleOutgoingBg = Color(0xFF163E32),
    bubbleOutgoingText = Color(0xFFF0F6FC),
    danger = Color(0xFFEF4444),
    dangerTint = Color(0xFF381C1C),
    isDark = true
)

// 3. Electric Cyan (Dark Mode)
private val ElectricCyanColors = ChatColors(
    background = Color(0xFF0A0E17),
    surface = Color(0xFF111827),
    secondarySurface = Color(0xFF1F2937),
    textPrimary = Color(0xFFF9FAFB),
    textMuted = Color(0xFF9CA3AF),
    border = Color(0xFF374151),
    subtleDivider = Color(0xFF1F2937),
    accent = Color(0xFF06B6D4),
    accentTint = Color(0xFF143542),
    accentDark = Color(0xFF22D3EE),
    statusSuccess = Color(0xFF10B981),
    statusSuccessTint = Color(0xFF132E27),
    bubbleIncomingBg = Color(0xFF1F2937),
    bubbleIncomingText = Color(0xFFF9FAFB),
    bubbleOutgoingBg = Color(0xFF153F4F),
    bubbleOutgoingText = Color(0xFFF9FAFB),
    danger = Color(0xFFEF4444),
    dangerTint = Color(0xFF381C1C),
    isDark = true
)

// 4. Royal Amethyst (Dark Mode)
private val RoyalAmethystColors = ChatColors(
    background = Color(0xFF120E18),
    surface = Color(0xFF1C1625),
    secondarySurface = Color(0xFF271F33),
    textPrimary = Color(0xFFF9FAFB),
    textMuted = Color(0xFFA79FB2),
    border = Color(0xFF3B304A),
    subtleDivider = Color(0xFF271F33),
    accent = Color(0xFFA855F7),
    accentTint = Color(0xFF311C47),
    accentDark = Color(0xFFC084FC),
    statusSuccess = Color(0xFF10B981),
    statusSuccessTint = Color(0xFF132E27),
    bubbleIncomingBg = Color(0xFF271F33),
    bubbleIncomingText = Color(0xFFF9FAFB),
    bubbleOutgoingBg = Color(0xFF452261),
    bubbleOutgoingText = Color(0xFFF9FAFB),
    danger = Color(0xFFEF4444),
    dangerTint = Color(0xFF381C1C),
    isDark = true
)

// 5. Crimson Rose (Dark Mode)
private val CrimsonRoseColors = ChatColors(
    background = Color(0xFF141010),
    surface = Color(0xFF1F1A1A),
    secondarySurface = Color(0xFF2A2424),
    textPrimary = Color(0xFFF9FAFB),
    textMuted = Color(0xFFA89F9F),
    border = Color(0xFF3D3232),
    subtleDivider = Color(0xFF2A2424),
    accent = Color(0xFFF43F5E),
    accentTint = Color(0xFF3B1C23),
    accentDark = Color(0xFFFB7185),
    statusSuccess = Color(0xFF10B981),
    statusSuccessTint = Color(0xFF132E27),
    bubbleIncomingBg = Color(0xFF2A2424),
    bubbleIncomingText = Color(0xFFF9FAFB),
    bubbleOutgoingBg = Color(0xFF5C1C28),
    bubbleOutgoingText = Color(0xFFF9FAFB),
    danger = Color(0xFFEF4444),
    dangerTint = Color(0xFF381C1C),
    isDark = true
)

// 6. Pearl Minimal (Light Mode Variant)
private val PearlMinimalColors = ChatColors(
    background = Color(0xFFF7F7F5),
    surface = Color(0xFFFFFFFF),
    secondarySurface = Color(0xFFEEEDEA),
    textPrimary = Color(0xFF29282D),
    textMuted = Color(0xFF77757D),
    border = Color(0xFFE3E2E5),
    subtleDivider = Color(0xFFEFEFEB),
    accent = Color(0xFF8D88C7),
    accentTint = Color(0xFFEAE8F5),
    accentDark = Color(0xFF7570AE),
    statusSuccess = Color(0xFF88A99A),
    statusSuccessTint = Color(0xFFE5EEEA),
    bubbleIncomingBg = Color(0xFFEEEDEA),
    bubbleIncomingText = Color(0xFF29282D),
    bubbleOutgoingBg = Color(0xFFEAE8F5),
    bubbleOutgoingText = Color(0xFF29282D),
    danger = Color(0xFFD46868),
    dangerTint = Color(0xFFFBEAEA),
    isDark = false
)

fun getColorsForPalette(palette: LuxuryPalette): ChatColors = when (palette) {
    LuxuryPalette.MidnightIris -> MidnightIrisColors
    LuxuryPalette.CyberEmerald -> CyberEmeraldColors
    LuxuryPalette.ElectricCyan -> ElectricCyanColors
    LuxuryPalette.RoyalAmethyst -> RoyalAmethystColors
    LuxuryPalette.CrimsonRose -> CrimsonRoseColors
    LuxuryPalette.PearlMinimal -> PearlMinimalColors
}

private fun ChatColors.toMaterialColorScheme(): ColorScheme {
    return if (isDark) {
        darkColorScheme(
            primary = accent,
            onPrimary = Color.White,
            primaryContainer = accentTint,
            onPrimaryContainer = textPrimary,
            secondary = secondarySurface,
            onSecondary = textPrimary,
            secondaryContainer = secondarySurface,
            onSecondaryContainer = textPrimary,
            tertiary = statusSuccess,
            onTertiary = Color.White,
            tertiaryContainer = statusSuccessTint,
            onTertiaryContainer = textPrimary,
            background = background,
            onBackground = textPrimary,
            surface = surface,
            onSurface = textPrimary,
            surfaceVariant = secondarySurface,
            onSurfaceVariant = textMuted,
            outline = border,
            outlineVariant = subtleDivider,
            error = danger,
            onError = Color.White,
            errorContainer = dangerTint,
            onErrorContainer = danger
        )
    } else {
        lightColorScheme(
            primary = accent,
            onPrimary = surface,
            primaryContainer = accentTint,
            onPrimaryContainer = textPrimary,
            secondary = secondarySurface,
            onSecondary = textPrimary,
            secondaryContainer = secondarySurface,
            onSecondaryContainer = textPrimary,
            tertiary = statusSuccess,
            onTertiary = surface,
            tertiaryContainer = statusSuccessTint,
            onTertiaryContainer = textPrimary,
            background = background,
            onBackground = textPrimary,
            surface = surface,
            onSurface = textPrimary,
            surfaceVariant = secondarySurface,
            onSurfaceVariant = textMuted,
            outline = border,
            outlineVariant = subtleDivider,
            error = danger,
            onError = surface,
            errorContainer = dangerTint,
            onErrorContainer = danger
        )
    }
}

val LocalChatColors = compositionLocalOf { MidnightIrisColors }

/**
 * Global Theme Controller - Default is Dark Mode (Midnight Iris)!
 */
object ThemeController {
    var currentPalette by mutableStateOf(LuxuryPalette.MidnightIris)

    fun nextTheme() {
        val entries = LuxuryPalette.entries
        val nextIdx = (entries.indexOf(currentPalette) + 1) % entries.size
        currentPalette = entries[nextIdx]
    }
}

object ChatTheme {
    val colors: ChatColors
        @Composable
        get() = LocalChatColors.current
}

@Composable
fun ChatApplicationTheme(
    palette: LuxuryPalette = ThemeController.currentPalette,
    content: @Composable () -> Unit
) {
    val customColors = getColorsForPalette(palette)
    val materialColorScheme = customColors.toMaterialColorScheme()

    CompositionLocalProvider(LocalChatColors provides customColors) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = Typography,
            content = content
        )
    }
}