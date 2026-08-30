package com.example.chatapplication.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
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
 * 5 Curated Quiet Luxury Palettes
 */
enum class LuxuryPalette(
    val title: String,
    val description: String,
    val previewColor: Color,
    val secondaryPreview: Color
) {
    QuietLavender(
        title = "Quiet Lavender",
        description = "Pearl neutrals with delicate soft lavender accents",
        previewColor = Color(0xFF8D88C7),
        secondaryPreview = Color(0xFFEAE8F5)
    ),
    NordicSage(
        title = "Nordic Sage",
        description = "Dew natural tones with calming eucalyptus sage accents",
        previewColor = Color(0xFF6E9887),
        secondaryPreview = Color(0xFFE4ECE8)
    ),
    CashmereSand(
        title = "Cashmere Sand",
        description = "Warm silk ivory with refined cashmere taupe accents",
        previewColor = Color(0xFF9C8578),
        secondaryPreview = Color(0xFFEFECE7)
    ),
    SlateMinimal(
        title = "Slate Minimal",
        description = "Clean modern studio mist with deep slate accents",
        previewColor = Color(0xFF626D7A),
        secondaryPreview = Color(0xFFE6E9EE)
    ),
    RoseQuartz(
        title = "Rose Quartz",
        description = "Subtle petal warm whites with understated muted rose accents",
        previewColor = Color(0xFFA67C8A),
        secondaryPreview = Color(0xFFF4EBF0)
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
    val dangerTint: Color
)

private val QuietLavenderColors = ChatColors(
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
    dangerTint = Color(0xFFFBEAEA)
)

private val NordicSageColors = ChatColors(
    background = Color(0xFFF5F7F5),
    surface = Color(0xFFFFFFFF),
    secondarySurface = Color(0xFFEAEFEA),
    textPrimary = Color(0xFF28302B),
    textMuted = Color(0xFF6F7D74),
    border = Color(0xFFDFE6E1),
    subtleDivider = Color(0xFFEAF0EC),
    accent = Color(0xFF6E9887),
    accentTint = Color(0xFFE4ECE8),
    accentDark = Color(0xFF5A8070),
    statusSuccess = Color(0xFF7AA894),
    statusSuccessTint = Color(0xFFE5EEEA),
    bubbleIncomingBg = Color(0xFFEAEFEA),
    bubbleIncomingText = Color(0xFF28302B),
    bubbleOutgoingBg = Color(0xFFE4ECE8),
    bubbleOutgoingText = Color(0xFF28302B),
    danger = Color(0xFFD46868),
    dangerTint = Color(0xFFFBEAEA)
)

private val CashmereSandColors = ChatColors(
    background = Color(0xFFF9F8F5),
    surface = Color(0xFFFFFFFF),
    secondarySurface = Color(0xFFEFECE6),
    textPrimary = Color(0xFF2F2B28),
    textMuted = Color(0xFF7D7771),
    border = Color(0xFFE5E1DA),
    subtleDivider = Color(0xFFEFECE6),
    accent = Color(0xFF9C8578),
    accentTint = Color(0xFFEFECE7),
    accentDark = Color(0xFF826C60),
    statusSuccess = Color(0xFF8A9E8D),
    statusSuccessTint = Color(0xFFE9EFEA),
    bubbleIncomingBg = Color(0xFFEFECE6),
    bubbleIncomingText = Color(0xFF2F2B28),
    bubbleOutgoingBg = Color(0xFFEFECE7),
    bubbleOutgoingText = Color(0xFF2F2B28),
    danger = Color(0xFFD46868),
    dangerTint = Color(0xFFFBEAEA)
)

private val SlateMinimalColors = ChatColors(
    background = Color(0xFFF6F7F9),
    surface = Color(0xFFFFFFFF),
    secondarySurface = Color(0xFFECEFF2),
    textPrimary = Color(0xFF262B33),
    textMuted = Color(0xFF6E7683),
    border = Color(0xFFDFE3E8),
    subtleDivider = Color(0xFFEBEEF2),
    accent = Color(0xFF626D7A),
    accentTint = Color(0xFFE6E9EE),
    accentDark = Color(0xFF4E5863),
    statusSuccess = Color(0xFF769C8E),
    statusSuccessTint = Color(0xFFE5ECE8),
    bubbleIncomingBg = Color(0xFFECEFF2),
    bubbleIncomingText = Color(0xFF262B33),
    bubbleOutgoingBg = Color(0xFFE6E9EE),
    bubbleOutgoingText = Color(0xFF262B33),
    danger = Color(0xFFD46868),
    dangerTint = Color(0xFFFBEAEA)
)

private val RoseQuartzColors = ChatColors(
    background = Color(0xFFF9F6F7),
    surface = Color(0xFFFFFFFF),
    secondarySurface = Color(0xFFEFE8EB),
    textPrimary = Color(0xFF31272B),
    textMuted = Color(0xFF7E7177),
    border = Color(0xFFE7DFE3),
    subtleDivider = Color(0xFFEFE7EB),
    accent = Color(0xFFA67C8A),
    accentTint = Color(0xFFF4EBF0),
    accentDark = Color(0xFF8D6673),
    statusSuccess = Color(0xFF839F92),
    statusSuccessTint = Color(0xFFE7EFEA),
    bubbleIncomingBg = Color(0xFFEFE8EB),
    bubbleIncomingText = Color(0xFF31272B),
    bubbleOutgoingBg = Color(0xFFF4EBF0),
    bubbleOutgoingText = Color(0xFF31272B),
    danger = Color(0xFFD46868),
    dangerTint = Color(0xFFFBEAEA)
)

fun getColorsForPalette(palette: LuxuryPalette): ChatColors = when (palette) {
    LuxuryPalette.QuietLavender -> QuietLavenderColors
    LuxuryPalette.NordicSage -> NordicSageColors
    LuxuryPalette.CashmereSand -> CashmereSandColors
    LuxuryPalette.SlateMinimal -> SlateMinimalColors
    LuxuryPalette.RoseQuartz -> RoseQuartzColors
}

private fun ChatColors.toMaterialColorScheme(): ColorScheme {
    return lightColorScheme(
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

val LocalChatColors = compositionLocalOf { QuietLavenderColors }

/**
 * Global Theme Controller for live switching
 */
object ThemeController {
    var currentPalette by mutableStateOf(LuxuryPalette.QuietLavender)
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