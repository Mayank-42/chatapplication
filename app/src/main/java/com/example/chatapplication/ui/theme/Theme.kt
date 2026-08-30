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
 * 8 Curated Distinctive Quiet Luxury Palettes
 */
enum class LuxuryPalette(
    val title: String,
    val subtitle: String,
    val description: String,
    val previewColor: Color,
    val secondaryPreview: Color,
    val isDark: Boolean = false
) {
    QuietLavender(
        title = "Quiet Lavender",
        subtitle = "Flagship Pearl",
        description = "Pearl neutrals with delicate soft lavender accents",
        previewColor = Color(0xFF8D88C7),
        secondaryPreview = Color(0xFFEAE8F5)
    ),
    NordicSage(
        title = "Nordic Sage",
        subtitle = "Calm Botanical",
        description = "Dew natural tones with calming eucalyptus sage accents",
        previewColor = Color(0xFF6E9887),
        secondaryPreview = Color(0xFFE4ECE8)
    ),
    CashmereSand(
        title = "Cashmere Sand",
        subtitle = "Warm Minimalist",
        description = "Warm silk ivory with refined cashmere taupe accents",
        previewColor = Color(0xFF9C8578),
        secondaryPreview = Color(0xFFEFECE7)
    ),
    SlateGlacier(
        title = "Slate & Glacier",
        subtitle = "Modern Studio",
        description = "Clean modern architectural mist with glacier slate accents",
        previewColor = Color(0xFF5C7C8D),
        secondaryPreview = Color(0xFFE2EAF0)
    ),
    RoseQuartz(
        title = "Rose Quartz",
        subtitle = "Haute Minimal",
        description = "Subtle petal warm whites with understated muted rose accents",
        previewColor = Color(0xFFA67C8A),
        secondaryPreview = Color(0xFFF4EBF0)
    ),
    OliveLinen(
        title = "Olive Linen",
        subtitle = "Quiet Earth",
        description = "Warm linen surfaces with soothing muted olive tones",
        previewColor = Color(0xFF7B8C74),
        secondaryPreview = Color(0xFFE2EBE0)
    ),
    TerracottaDune(
        title = "Desert Clay",
        subtitle = "Warm Mineral",
        description = "Soft sandstone surfaces with calm muted terracotta accents",
        previewColor = Color(0xFFA37B74),
        secondaryPreview = Color(0xFFF2EAE7)
    ),
    ObsidianOnyx(
        title = "Obsidian Titanium",
        subtitle = "Low-Luminance Luxury",
        description = "Sleek dark titanium surfaces with soft iris glowing accents",
        previewColor = Color(0xFFA8A3E0),
        secondaryPreview = Color(0xFF2D2B3D),
        isDark = true
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
    val isDark: Boolean = false
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
    dangerTint = Color(0xFFFBEAEA),
    isDark = false
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
    dangerTint = Color(0xFFFBEAEA),
    isDark = false
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
    dangerTint = Color(0xFFFBEAEA),
    isDark = false
)

private val SlateGlacierColors = ChatColors(
    background = Color(0xFFF5F7F9),
    surface = Color(0xFFFFFFFF),
    secondarySurface = Color(0xFFE8ECF0),
    textPrimary = Color(0xFF20262E),
    textMuted = Color(0xFF66717E),
    border = Color(0xFFDCE2E8),
    subtleDivider = Color(0xFFEAEFF4),
    accent = Color(0xFF5C7C8D),
    accentTint = Color(0xFFE2EAF0),
    accentDark = Color(0xFF486473),
    statusSuccess = Color(0xFF749B8D),
    statusSuccessTint = Color(0xFFE3EFE9),
    bubbleIncomingBg = Color(0xFFE8ECF0),
    bubbleIncomingText = Color(0xFF20262E),
    bubbleOutgoingBg = Color(0xFFE2EAF0),
    bubbleOutgoingText = Color(0xFF20262E),
    danger = Color(0xFFD46868),
    dangerTint = Color(0xFFFBEAEA),
    isDark = false
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
    dangerTint = Color(0xFFFBEAEA),
    isDark = false
)

private val OliveLinenColors = ChatColors(
    background = Color(0xFFF7F8F5),
    surface = Color(0xFFFFFFFF),
    secondarySurface = Color(0xFFEBEFE8),
    textPrimary = Color(0xFF272D24),
    textMuted = Color(0xFF6E7869),
    border = Color(0xFFDEE4D9),
    subtleDivider = Color(0xFFEAF0E6),
    accent = Color(0xFF7B8C74),
    accentTint = Color(0xFFE2EBE0),
    accentDark = Color(0xFF64735E),
    statusSuccess = Color(0xFF7AA894),
    statusSuccessTint = Color(0xFFE5EEEA),
    bubbleIncomingBg = Color(0xFFEBEFE8),
    bubbleIncomingText = Color(0xFF272D24),
    bubbleOutgoingBg = Color(0xFFE2EBE0),
    bubbleOutgoingText = Color(0xFF272D24),
    danger = Color(0xFFD46868),
    dangerTint = Color(0xFFFBEAEA),
    isDark = false
)

private val TerracottaDuneColors = ChatColors(
    background = Color(0xFFF9F7F5),
    surface = Color(0xFFFFFFFF),
    secondarySurface = Color(0xFFEFEAE7),
    textPrimary = Color(0xFF302826),
    textMuted = Color(0xFF7E7370),
    border = Color(0xFFE6DFDC),
    subtleDivider = Color(0xFFEFE9E6),
    accent = Color(0xFFA37B74),
    accentTint = Color(0xFFF2EAE7),
    accentDark = Color(0xFF8A645D),
    statusSuccess = Color(0xFF88A99A),
    statusSuccessTint = Color(0xFFE5EEEA),
    bubbleIncomingBg = Color(0xFFEFEAE7),
    bubbleIncomingText = Color(0xFF302826),
    bubbleOutgoingBg = Color(0xFFF2EAE7),
    bubbleOutgoingText = Color(0xFF302826),
    danger = Color(0xFFD46868),
    dangerTint = Color(0xFFFBEAEA),
    isDark = false
)

private val ObsidianOnyxColors = ChatColors(
    background = Color(0xFF141416),
    surface = Color(0xFF1E1E22),
    secondarySurface = Color(0xFF27272D),
    textPrimary = Color(0xFFF0F0F2),
    textMuted = Color(0xFF9898A4),
    border = Color(0xFF32323A),
    subtleDivider = Color(0xFF26262E),
    accent = Color(0xFFA8A3E0),
    accentTint = Color(0xFF2A283C),
    accentDark = Color(0xFFC0BCF5),
    statusSuccess = Color(0xFF88C9AE),
    statusSuccessTint = Color(0xFF1C2D26),
    bubbleIncomingBg = Color(0xFF27272D),
    bubbleIncomingText = Color(0xFFF0F0F2),
    bubbleOutgoingBg = Color(0xFF2D2A42),
    bubbleOutgoingText = Color(0xFFF0F0F2),
    danger = Color(0xFFE57373),
    dangerTint = Color(0xFF381F1F),
    isDark = true
)

fun getColorsForPalette(palette: LuxuryPalette): ChatColors = when (palette) {
    LuxuryPalette.QuietLavender -> QuietLavenderColors
    LuxuryPalette.NordicSage -> NordicSageColors
    LuxuryPalette.CashmereSand -> CashmereSandColors
    LuxuryPalette.SlateGlacier -> SlateGlacierColors
    LuxuryPalette.RoseQuartz -> RoseQuartzColors
    LuxuryPalette.OliveLinen -> OliveLinenColors
    LuxuryPalette.TerracottaDune -> TerracottaDuneColors
    LuxuryPalette.ObsidianOnyx -> ObsidianOnyxColors
}

private fun ChatColors.toMaterialColorScheme(): ColorScheme {
    return if (isDark) {
        darkColorScheme(
            primary = accent,
            onPrimary = Color.Black,
            primaryContainer = accentTint,
            onPrimaryContainer = textPrimary,
            secondary = secondarySurface,
            onSecondary = textPrimary,
            secondaryContainer = secondarySurface,
            onSecondaryContainer = textPrimary,
            tertiary = statusSuccess,
            onTertiary = Color.Black,
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
            onError = Color.Black,
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

val LocalChatColors = compositionLocalOf { QuietLavenderColors }

/**
 * Global Theme Controller for live switching across all pages
 */
object ThemeController {
    var currentPalette by mutableStateOf(LuxuryPalette.QuietLavender)

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