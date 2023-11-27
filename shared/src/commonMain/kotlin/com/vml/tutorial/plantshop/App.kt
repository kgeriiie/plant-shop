package com.vml.tutorial.plantshop

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.vml.tutorial.plantshop.core.presentation.PlantShopTheme
import com.vml.tutorial.plantshop.di.AppModule
import com.vml.tutorial.plantshop.plants.presentation.home.components.HomeScreen
import com.vml.tutorial.plantshop.plants.presentation.home.components.HomeScreenViewModel
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun App(
    appModule: AppModule
) {
    PlantShopTheme {
        val homeScreenViewModel = getViewModel(
            key = "home-screen",
            factory = viewModelFactory {
                HomeScreenViewModel(appModule.plantsDataSource.getPlants())
            }
        )
        val homeScreenState by homeScreenViewModel.state.collectAsState()

        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            HomeScreen(homeScreenState, homeScreenViewModel::onEvent)
        }
    }
}