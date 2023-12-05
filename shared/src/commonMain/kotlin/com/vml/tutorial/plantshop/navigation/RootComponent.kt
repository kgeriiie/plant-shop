package com.vml.tutorial.plantshop.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.vml.tutorial.plantshop.di.AppModule
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.detail.PlantDetailComponent
import com.vml.tutorial.plantshop.plants.presentation.home.HomeScreenComponent
import kotlinx.serialization.Serializable

class RootComponent(
    componentContext: ComponentContext,
    private val appModule: AppModule,
): ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()
    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.HomeScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(
        config: Configuration,
        context: ComponentContext
    ): Child {
        return when(config) {
            Configuration.HomeScreen -> Child.HomeScreen(HomeScreenComponent(
                componentContext = context,
                plantsRepository = appModule.plantsRepository,
                onNavigateToDetail = { plant ->
                    navigation.pushNew(Configuration.PlantDetailScreen(plant))
                }
            ))
            is Configuration.PlantDetailScreen -> Child.DetailScreen(
                PlantDetailComponent(
                plant = config.plant,
                componentContext =  context,
                    onNavigateToBack = {
                        navigation.pop()
                    }
            ))
        }
    }

    sealed class Child {
        data class HomeScreen(val component: HomeScreenComponent): Child()
        data class DetailScreen(val component: PlantDetailComponent): Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object HomeScreen: Configuration()
        @Serializable
        data class PlantDetailScreen(val plant: Plant): Configuration()
    }
}