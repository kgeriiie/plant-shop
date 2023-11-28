package com.vml.tutorial.plantshop

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.vml.tutorial.plantshop.core.presentation.PlantShopTheme
import com.vml.tutorial.plantshop.di.AppModule
import com.vml.tutorial.plantshop.plants.presentation.detail.PlantDetailViewModel
import com.vml.tutorial.plantshop.plants.presentation.detail.components.PlantDetailScreen
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun App(
    appModule: AppModule
) {
    PlantShopTheme {
        val plantDetailViewModel = getViewModel(
            key = "plant-detail-screeen",
            factory = viewModelFactory {
                PlantDetailViewModel(appModule.plantsDataSource.getPlants().first())
            }
        )
        val plantDetailState by plantDetailViewModel.state.collectAsState()

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            // Fetch plants -> appModule.plantsDataSource.getPlants()
            //HomeScreen()
            PlantDetailScreen(plantDetailState, plantDetailViewModel::onEvent)
        }
    }
}