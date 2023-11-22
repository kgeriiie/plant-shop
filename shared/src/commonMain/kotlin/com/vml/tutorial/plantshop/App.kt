package com.vml.tutorial.plantshop

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.vml.tutorial.plantshop.core.presentation.PlantShopTheme
import com.vml.tutorial.plantshop.di.AppModule
import com.vml.tutorial.plantshop.plants.presentation.home.components.HomeScreen

@Composable
fun App(
    appModule: AppModule
) {
    PlantShopTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            // Fetch plants -> appModule.plantsDataSource.getPlants()
            HomeScreen()
        }
    }
}