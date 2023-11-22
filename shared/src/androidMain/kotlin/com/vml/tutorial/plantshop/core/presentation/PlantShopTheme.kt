package com.vml.tutorial.plantshop.core.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.vml.tutorial.plantshop.ui.theme.LightColors


@Composable
actual fun PlantShopTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}