package com.vml.tutorial.plantshop.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight

// https://developer.android.com/jetpack/compose/designsystems/material2-material3?hl=en#typography
private val defaultTypography = Typography()
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge,
    displayMedium = defaultTypography.displayMedium.copy(fontWeight = FontWeight.Bold),
    displaySmall = defaultTypography.displaySmall,
    headlineLarge = defaultTypography.headlineLarge.copy(fontWeight = FontWeight.Bold),
    headlineMedium = defaultTypography.headlineMedium.copy(fontWeight = FontWeight.Bold),
    headlineSmall = defaultTypography.headlineSmall.copy(fontWeight = FontWeight.Bold),
    titleLarge = defaultTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
    titleMedium = defaultTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
    titleSmall = defaultTypography.titleSmall.copy(fontWeight = FontWeight.Bold),
    bodyLarge = defaultTypography.bodyLarge,
    bodyMedium = defaultTypography.bodyMedium,
    bodySmall = defaultTypography.bodySmall,
    labelLarge = defaultTypography.labelLarge,
    labelMedium = defaultTypography.labelMedium,
    labelSmall = defaultTypography.labelSmall
)
