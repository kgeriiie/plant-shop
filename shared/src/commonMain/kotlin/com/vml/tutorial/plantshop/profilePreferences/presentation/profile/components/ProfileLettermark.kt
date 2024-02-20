package com.vml.tutorial.plantshop.profilePreferences.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.profilePreferences.domain.User
import com.vml.tutorial.plantshop.ui.theme.Typography

@Composable
fun ProfileLettermark(
    user: User?,
    textStyle: TextStyle = Typography.displayMedium,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
) {
    val roundedCornerShape = remember { RoundedCornerShape(16.dp) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.shadow(elevation = 4.dp, shape = roundedCornerShape)
            .clip(roundedCornerShape)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable(enabled = enabled, onClick = onClicked)
    ) {
        user?.let {
            Text(
                text = "${it.firstName?.first()} ${it.lastName?.first()}".uppercase(),
                style = textStyle
            )
        }
    }
}
