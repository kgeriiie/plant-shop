package com.vml.tutorial.plantshop.main.presentation
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.vml.tutorial.plantshop.basket.presentation.BasketComponent
import com.vml.tutorial.plantshop.di.AppModule
import com.vml.tutorial.plantshop.favourites.presentation.FavouritesComponent
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.detail.PlantDetailComponent
import com.vml.tutorial.plantshop.plants.presentation.home.HomeScreenComponent
import kotlinx.serialization.Serializable

class MainComponent(
    componentContext: ComponentContext,
    private val appModule: AppModule
): ComponentContext by componentContext {

    private val navigation = StackNavigation<Configuration>()
    val childStack = childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.HomeScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private val _navBarVisibility: MutableValue<Boolean> = MutableValue(true)
    var navigationBarVisible: Value<Boolean> = _navBarVisibility

    fun navigateTo(destination: Configuration) {
        navigation.pushNew(destination) { success ->
            _navBarVisibility.value = true
        }
    }

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(
        config: Configuration,
        context: ComponentContext
    ): Child {
        return when (config) {
            Configuration.BasketScreen -> Child.BasketScreen(BasketComponent(componentContext = context))
            Configuration.FavouritesScreen -> Child.FavouritesScreen(FavouritesComponent(componentContext = context))
            Configuration.HomeScreen -> Child.HomeScreen(HomeScreenComponent(
                componentContext = context,
                plantsRepository = appModule.plantsRepository,
                onNavigateToDetail = { plant ->
                    _navBarVisibility.value = false
                    navigation.pushNew(Configuration.PlantDetailScreen(plant))
                }
            ))
            is Configuration.PlantDetailScreen -> Child.PlantDetailScreen(
                PlantDetailComponent(
                plant = config.plant,
                componentContext =  context,
                    onNavigateToBack = {
                        _navBarVisibility.value = true
                        navigation.pop()
                    }
            ))
        }
    }

    sealed class Child {
        data class HomeScreen(val component: HomeScreenComponent): Child()
        data class FavouritesScreen(val component: FavouritesComponent): Child()
        data class BasketScreen(val component: BasketComponent): Child()
        data class PlantDetailScreen(val component: PlantDetailComponent): Child()
    }

    @Serializable
    sealed class Configuration {
        @Serializable
        data object HomeScreen: Configuration()
        @Serializable
        data object FavouritesScreen: Configuration()
        @Serializable
        data object BasketScreen: Configuration()
        @Serializable
        data class PlantDetailScreen(val plant: Plant): Configuration()
    }
}