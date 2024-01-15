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
import com.vml.tutorial.plantshop.login.presentation.LoginScreen
import com.vml.tutorial.plantshop.main.presentation.MainScreen
import com.vml.tutorial.plantshop.splash.presentation.SplashScreen

@Composable
fun App(
    root: AppComponent
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
                    is AppComponent.Child.SplashScreen -> SplashScreen(instance.component)
                    is AppComponent.Child.MainScreen -> MainScreen(instance.component)
                    is AppComponent.Child.LoginScreen -> {
                        val state by instance.component.uiState.collectAsState()
                        LoginScreen(state) { uiEvent ->
                            instance.component.onEvent(uiEvent)
                        }
                    }
                }
            }
        }
    }
}
