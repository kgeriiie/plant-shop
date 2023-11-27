package com.vml.tutorial.plantshop.plants.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.vml.tutorial.plantshop.core.presentation.UiText
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import com.vml.tutorial.plantshop.MR.strings.plant_image_description
import com.vml.tutorial.plantshop.core.presentation.asString

@Composable
fun PlantImage(
    url: String,
    contentScale: ContentScale = ContentScale.Crop,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        KamelImage(
            resource = asyncPainterResource(url),
            contentScale = contentScale,
            contentDescription = UiText.StringRes(plant_image_description).asString(),
            onLoading = { CircularProgressIndicator() }
        )
    }
}