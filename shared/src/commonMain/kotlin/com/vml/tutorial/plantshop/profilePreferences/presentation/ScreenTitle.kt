package com.vml.tutorial.plantshop.profilePreferences.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.vml.tutorial.plantshop.ui.theme.Typography

@Composable
fun ToolbarSection(titleText: String, subtitleText: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            text = titleText,
            modifier = Modifier.fillMaxWidth(),
            style = Typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
        Text(
            text = subtitleText,
            modifier = Modifier.fillMaxWidth(),
            style = Typography.titleMedium,
            color = Color.Black.copy(alpha = 0.6f),
        )
    }
}