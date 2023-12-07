package com.vml.tutorial.plantshop.main.presentation
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.vml.tutorial.plantshop.basket.presentation.BasketComponent
import com.vml.tutorial.plantshop.core.utils.Logger
import com.vml.tutorial.plantshop.di.AppModule
import com.vml.tutorial.plantshop.favourites.presentation.FavouritesComponent
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.detail.PlantDetailComponent
import com.vml.tutorial.plantshop.plants.presentation.home.HomeScreenComponent
import com.vml.tutorial.plantshop.plants.presentation.home.HomeScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable

interface MainComponent {
    val childStack: Value<ChildStack<*, MainChild>>
    var state: StateFlow<MainScreenState>

    fun onEvent(event: MainScreenEvent)
    sealed class MainChild{
        data class HomeScreen(val component: HomeScreenComponent): MainChild()
        data class FavouritesScreen(val component: FavouritesComponent): MainChild()
        data class BasketScreen(val component: BasketComponent): MainChild()
        data class PlantDetailScreen(val component: PlantDetailComponent): MainChild()
    }
}

class DefaultMainComponent(
    componentContext: ComponentContext,
    private val appModule: AppModule
): MainComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<MainConfiguration>()
    override val childStack = childStack(
        source = navigation,
        serializer = MainConfiguration.serializer(),
        initialConfiguration = MainConfiguration.HomeScreen,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private val _state: MutableStateFlow<MainScreenState> = MutableStateFlow(MainScreenState())
    override var state: StateFlow<MainScreenState> = _state.asStateFlow()

    override fun onEvent(event: MainScreenEvent) {
        when(event) {
            MainScreenEvent.OnBasketTabClicked -> navigateTab(MainConfiguration.BasketScreen)
            MainScreenEvent.OnFavouriteTabClicked -> navigateTab(MainConfiguration.FavouritesScreen)
            MainScreenEvent.OnHomeTabClicked -> navigateTab(MainConfiguration.HomeScreen)
        }
    }

    private fun navigateTab(configuration: MainConfiguration) {
        _state.update { it.copy(bottomNavigationVisible = true) }
        navigation.replaceCurrent(configuration)
    }

    @OptIn(ExperimentalDecomposeApi::class)
    private fun createChild(
        config: MainConfiguration,
        context: ComponentContext
    ): MainComponent.MainChild {
        return when (config) {
            MainConfiguration.BasketScreen -> MainComponent.MainChild.BasketScreen(BasketComponent(componentContext = context))
            MainConfiguration.FavouritesScreen -> MainComponent.MainChild.FavouritesScreen(FavouritesComponent(componentContext = context))
            MainConfiguration.HomeScreen -> MainComponent.MainChild.HomeScreen(HomeScreenComponent(
                componentContext = context,
                plantsRepository = appModule.plantsRepository,
                onNavigateToDetail = { plant ->
                    _state.update { it.copy(bottomNavigationVisible = false) }
                    navigation.pushNew(MainConfiguration.PlantDetailScreen(plant))
                }
            ))
            is MainConfiguration.PlantDetailScreen -> MainComponent.MainChild.PlantDetailScreen(
                PlantDetailComponent(
                    plant = config.plant,
                    componentContext =  context,
                    onNavigateToBack = {
                        _state.update { it.copy(bottomNavigationVisible = true) }
                        navigation.pop()
                    }
            ))
        }
    }

    @Serializable
    sealed class MainConfiguration {
        @Serializable
        data object HomeScreen: MainConfiguration()
        @Serializable
        data object FavouritesScreen: MainConfiguration()
        @Serializable
        data object BasketScreen: MainConfiguration()
        @Serializable
        data class PlantDetailScreen(val plant: Plant): MainConfiguration()
    }

    data class MainUiState(
        val bottomNavigationVisible: Boolean = true
    )
}