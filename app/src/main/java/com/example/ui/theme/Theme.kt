package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
  primary = PrimarySage,
  onPrimary = OnPrimarySage,
  primaryContainer = PrimaryContainerSage,
  onPrimaryContainer = OnPrimaryContainerSage,
  secondary = SecondaryClay,
  onSecondary = OnSecondaryClay,
  secondaryContainer = SecondaryContainerClay,
  onSecondaryContainer = OnSecondaryContainerClay,
  tertiary = AccentCoral,
  onTertiary = OnAccentCoral,
  tertiaryContainer = AccentContainerCoral,
  onTertiaryContainer = OnAccentContainerCoral,
  background = BackgroundCream,
  onBackground = OnBackgroundCacao,
  surface = SurfaceWhite,
  onSurface = OnSurfaceCacao,
  surfaceVariant = SurfaceVariantWarm,
  onSurfaceVariant = OnSurfaceVariantWarm,
  outline = BorderWarm
)

private val DarkColorScheme = darkColorScheme(
  primary = PrimarySageDark,
  onPrimary = OnPrimarySageDark,
  primaryContainer = PrimaryContainerSageDark,
  onPrimaryContainer = OnPrimaryContainerSageDark,
  secondary = SecondaryClayDark,
  onSecondary = OnSecondaryClayDark,
  secondaryContainer = SecondaryContainerClayDark,
  onSecondaryContainer = OnSecondaryContainerClayDark,
  tertiary = AccentCoralDark,
  onTertiary = OnAccentCoralDark,
  tertiaryContainer = AccentContainerCoralDark,
  onTertiaryContainer = OnAccentContainerCoralDark,
  background = BackgroundDark,
  onBackground = OnBackgroundDark,
  surface = SurfaceDark,
  onSurface = OnSurfaceDark,
  surfaceVariant = SurfaceVariantDark,
  onSurfaceVariant = OnSurfaceVariantDark,
  outline = BorderDark
)

@Composable
fun NutricionDRTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
