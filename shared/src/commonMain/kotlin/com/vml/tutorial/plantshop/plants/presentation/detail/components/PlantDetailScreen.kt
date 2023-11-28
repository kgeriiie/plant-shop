package com.vml.tutorial.plantshop.plants.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.plants.presentation.PlantImage
import com.vml.tutorial.plantshop.plants.presentation.detail.PlantDetailEvent
import com.vml.tutorial.plantshop.plants.presentation.detail.PlantDetailState

@Composable
fun PlantDetailScreen(
    state: PlantDetailState,
    onEvent: (PlantDetailEvent) -> Unit
) {
    Scaffold {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            PlantImage(
                url = state.plant.image,
                modifier = Modifier.size(200.dp, 200.dp)
            )
        }
    }
}