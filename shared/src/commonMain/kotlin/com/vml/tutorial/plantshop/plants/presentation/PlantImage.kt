package com.vml.tutorial.plantshop.plants.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.vml.tutorial.plantshop.core.presentation.UiText
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import com.vml.tutorial.plantshop.MR.strings.plant_image_description
import com.vml.tutorial.plantshop.core.presentation.asString
import io.kamel.core.Resource
import io.kamel.core.utils.cacheControl
import io.ktor.http.CacheControl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job

@Composable
fun PlantImage(
    url: String,
    contentScale: ContentScale = ContentScale.Crop,
    modifier: Modifier = Modifier
) {
    val painterResource: Resource<Painter> = asyncPainterResource(url) {
        coroutineContext = Job() + Dispatchers.IO
        requestBuilder {
            cacheControl(CacheControl.MaxAge(maxAgeSeconds = 60))
        }
    }

    KamelImage(
        modifier = modifier,
        resource = painterResource,
        contentScale = contentScale,
        contentDescription = UiText.StringRes(plant_image_description).asString(),
        onLoading = { CircularProgressIndicator() }
    )
}