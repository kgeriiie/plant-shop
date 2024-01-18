package com.vml.tutorial.plantshop.splash.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.ui.theme.Typography
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun SplashScreen(
    state: SplashUiState,
    onStart: () -> Unit
) {
    Box(
      modifier = Modifier
          .fillMaxSize()
          .background(Color.Green)
    ) {
        Image(
            painter = painterResource(MR.images.bg_splash),
            contentDescription = UiText.StringRes(MR.strings.background_image).asString(),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            if (state.loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            Column {
                Text(
                    text = UiText.StringRes(MR.strings.splash_title_text).asString(),
                    style = Typography.displayLarge,
                    fontWeight =FontWeight.Bold,
                    color = Color.Black,
                )

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = UiText.StringRes(MR.strings.splash_subtitle_text).asString(),
                    style = Typography.titleSmall,
                    color = Color.Black.copy(alpha = 0.6f),
                )

                if (state.buttonEnabled) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = { onStart.invoke() }) {
                        Text(
                            text = UiText.StringRes(MR.strings.splash_get_started_button_text).asString(),
                            style = Typography.titleSmall
                        )
                    }
                }
            }
        }
    }
}