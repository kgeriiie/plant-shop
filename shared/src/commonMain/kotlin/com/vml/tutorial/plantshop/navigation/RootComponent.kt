package com.vml.tutorial.plantshop.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pushNew
import com.vml.tutorial.plantshop.di.AppModule
import com.vml.tutorial.plantshop.main.presentation.MainComponent
import com.vml.tutorial.plantshop.splash.presentation.SplashComponent
import kotlinx.serialization.Serializable

class RootComponent(
    componentContext: ComponentContext,
    private val appModule: AppModule,
): ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()
    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.SplashScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(
        config: Configuration,
        context: ComponentContext
    ): Child {
        return when(config) {
            Configuration.MainScreen -> Child.MainScreen(MainComponent(context, appModule))
            Configuration.SplashScreen -> Child.SplashScreen(SplashComponent(context) {
                navigation.pushNew(Configuration.MainScreen)
            })
        }
    }

    sealed class Child {
        data class SplashScreen(val component: SplashComponent): Child()
        data class MainScreen(val component: MainComponent): Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object SplashScreen: Configuration()
        @Serializable
        data object MainScreen: Configuration()

    }
}