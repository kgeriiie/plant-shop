package com.vml.tutorial.plantshop

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.vml.tutorial.plantshop.core.presentation.PlantShopTheme
import com.vml.tutorial.plantshop.main.presentation.MainScreen
import com.vml.tutorial.plantshop.navigation.RootComponent
import com.vml.tutorial.plantshop.splash.presentation.SplashScreen

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
                    is RootComponent.Child.MainScreen -> MainScreen(instance.component)
                    is RootComponent.Child.SplashScreen -> SplashScreen(instance.component)
                }
            }
        }
    }
}
