package com.vml.tutorial.plantshop.profilePreferences.presentation.getHelp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.presentation.LoadingButton
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.profilePreferences.presentation.TitleSection
import com.vml.tutorial.plantshop.ui.theme.Typography
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun GetHelpScreen(onEvent: (GetHelpEvent) -> Unit) {
    Column(
        modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(modifier = Modifier.size(50.dp), onClick = {
            onEvent(GetHelpEvent.NavigateBack)
        }) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        TitleSection(
            UiText.StringRes(MR.strings.get_help_screen_title_text).asString(),
            UiText.StringRes(MR.strings.get_help_title_subtitle_text).asString(),
            Modifier.padding(horizontal = 16.dp)
        )
        Message { onEvent(GetHelpEvent.CallClicked) }
    }
}

@Composable
private fun Message(modifier: Modifier = Modifier, onCallButtonClicked: () -> Unit) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(72.dp))
        Icon(
            painter = painterResource(MR.images.ic_support),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = UiText.StringRes(MR.strings.background_image).asString(),
            modifier = Modifier.size(200.dp, 200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = UiText.StringRes(MR.strings.get_help_message).asString(),
            textAlign = TextAlign.Center,
            style = Typography.titleMedium,
            fontWeight = FontWeight.Normal,
        )
        Spacer(modifier = Modifier.height(16.dp))
        LoadingButton(UiText.StringRes(MR.strings.get_help_button_text).asString(), false) {
            onCallButtonClicked()
        }
    }
}
