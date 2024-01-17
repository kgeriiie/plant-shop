package com.vml.tutorial.plantshop.profile.presentation.components

import androidx.compose.foundation.background
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
import com.vml.tutorial.plantshop.profile.domain.User
import com.vml.tutorial.plantshop.ui.theme.Typography

@Composable
fun ProfilePhoto(
    user: User?,
    textStyle: TextStyle = Typography.displayMedium,
    modifier: Modifier = Modifier
) {
    val roundedCornerShape = remember { RoundedCornerShape(16.dp) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.shadow(elevation = 4.dp, shape = roundedCornerShape)
            .clip(roundedCornerShape)
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Text(
            text = "${user?.firstName?.first()} ${user?.lastName?.first()}".uppercase(),
            style = textStyle
        )
    }
}
