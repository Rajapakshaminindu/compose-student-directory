package com.example.activity_03.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ── Brand color palette ───────────────────────────────────────────────────────
// Derived from a deep indigo-teal palette for an academic / professional look.

private val Indigo900  = Color(0xFF1A237E)
private val Indigo700  = Color(0xFF303F9F)
private val Indigo500  = Color(0xFF3F51B5)
private val Indigo200  = Color(0xFF9FA8DA)
private val Indigo50   = Color(0xFFE8EAF6)

private val Teal600    = Color(0xFF00897B)
private val Teal200    = Color(0xFF80CBC4)

private val ErrorRed   = Color(0xFFB00020)
private val ErrorRedDk = Color(0xFFCF6679)

// ── Color schemes ─────────────────────────────────────────────────────────────

private val LightColors = lightColorScheme(
    primary             = Indigo700,
    onPrimary           = Color.White,
    primaryContainer    = Indigo50,
    onPrimaryContainer  = Indigo900,
    secondary           = Teal600,
    onSecondary         = Color.White,
    secondaryContainer  = Teal200.copy(alpha = 0.3f),
    onSecondaryContainer= Color(0xFF00352F),
    error               = ErrorRed,
    onError             = Color.White,
    errorContainer      = Color(0xFFFCD8DC),
    onErrorContainer    = Color(0xFF410014),
    background          = Color(0xFFF5F5F5),
    onBackground        = Color(0xFF1A1A2E),
    surface             = Color.White,
    onSurface           = Color(0xFF1A1A2E),
    surfaceVariant      = Indigo50,
    onSurfaceVariant    = Color(0xFF444777),
    outline             = Indigo200
)

private val DarkColors = darkColorScheme(
    primary             = Indigo200,
    onPrimary           = Indigo900,
    primaryContainer    = Indigo700,
    onPrimaryContainer  = Indigo50,
    secondary           = Teal200,
    onSecondary         = Color(0xFF00352F),
    secondaryContainer  = Teal600,
    onSecondaryContainer= Teal200,
    error               = ErrorRedDk,
    onError             = Color(0xFF690021),
    errorContainer      = Color(0xFF930028),
    onErrorContainer    = ErrorRedDk,
    background          = Color(0xFF121212),
    onBackground        = Color(0xFFE1E2EC),
    surface             = Color(0xFF1E1E2E),
    onSurface           = Color(0xFFE1E2EC),
    surfaceVariant      = Color(0xFF2A2A3E),
    onSurfaceVariant    = Indigo200,
    outline             = Color(0xFF545478)
)

// ── Theme composable ──────────────────────────────────────────────────────────

/**
 * Root Material 3 theme for the Student Directory application.
 *
 * On Android 12+ (API 31) with [dynamicColor] enabled, the palette is derived
 * from the user's wallpaper. On older API levels the custom indigo-teal palette
 * is used instead.
 *
 * @param darkTheme     Whether to use the dark colour scheme. Defaults to the system setting.
 * @param dynamicColor  Whether to opt-in to Material You dynamic colour (Android 12+).
 * @param content       The composable content to render inside the theme.
 */
@Composable
fun StudentDirectoryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else      -> LightColors
    }

    // Keep the status-bar icons dark on light backgrounds, light on dark backgrounds.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}
