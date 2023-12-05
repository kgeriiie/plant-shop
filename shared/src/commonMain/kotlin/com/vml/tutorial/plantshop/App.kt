package com.vml.tutorial.plantshop

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.vml.tutorial.plantshop.core.presentation.PlantShopTheme
import com.vml.tutorial.plantshop.di.AppModule
import com.vml.tutorial.plantshop.navigation.RootComponent
import com.vml.tutorial.plantshop.plants.presentation.detail.PlantDetailViewModel
import com.vml.tutorial.plantshop.plants.presentation.detail.components.PlantDetailScreen
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import com.vml.tutorial.plantshop.plants.presentation.home.components.HomeScreen
import com.vml.tutorial.plantshop.plants.presentation.home.HomeScreenViewModel
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun App(
    root: RootComponent
) {
    PlantShopTheme {
        val childStack by root.childStack.subscribeAsState()
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Children(
                stack = childStack,
                animation = stackAnimation(slide())
            ) { child ->
                when(val instance = child.instance) {
                    is RootComponent.Child.DetailScreen -> {
                        val plantDetailState by instance.component.state.collectAsState()
                        PlantDetailScreen(plantDetailState) { event ->
                            instance.component.onEvent(event)
                        }
                    }
                    is RootComponent.Child.HomeScreen -> {
                        val homeState by instance.component.state.collectAsState()
                        HomeScreen(homeState) { event ->
                            instance.component.onEvent(event)
                        }
                    }
                }
            }
        }
    }
}
