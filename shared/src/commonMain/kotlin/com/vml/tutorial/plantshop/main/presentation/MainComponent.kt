package com.vml.tutorial.plantshop.main.presentation
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.vml.tutorial.plantshop.basket.presentation.BasketComponent
import com.vml.tutorial.plantshop.basket.presentation.components.BasketEvent
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.di.AppModule
import com.vml.tutorial.plantshop.plants.presentation.favourites.FavouritesComponent
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.detail.PlantDetailComponent
import com.vml.tutorial.plantshop.plants.presentation.detail.PlantDetailEvent
import com.vml.tutorial.plantshop.plants.presentation.home.HomeScreenComponent
import com.vml.tutorial.plantshop.profile.orders.presentation.OrderHistoryComponent
import com.vml.tutorial.plantshop.profile.domain.User
import com.vml.tutorial.plantshop.profile.orders.data.usecase.OrderPlantsUseCase
import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem
import com.vml.tutorial.plantshop.profile.orders.domain.OrderStatus
import com.vml.tutorial.plantshop.profile.orders.presentation.all.OrderHistoryAllComponent
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderHistoryEvents
import com.vml.tutorial.plantshop.profile.orders.presentation.track.TrackOrderComponent
import com.vml.tutorial.plantshop.profile.orders.presentation.track.TrackOrderEvents
import com.vml.tutorial.plantshop.profile.presentation.ProfileComponent
import com.vml.tutorial.plantshop.profile.presentation.components.ProfileEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

interface MainComponent {
    val childStack: Value<ChildStack<*, MainChild>>
    var state: StateFlow<MainScreenState>
    val actions: Flow<DefaultMainComponent.Actions>

    fun onEvent(event: MainScreenEvent)
    sealed class MainChild{
        data class HomeScreen(val component: HomeScreenComponent): MainChild()
        data class FavouritesScreen(val component: FavouritesComponent): MainChild()
        data class BasketScreen(val component: BasketComponent): MainChild()
        data class PlantDetailScreen(val component: PlantDetailComponent): MainChild()
        data class ProfileScreen(val component: ProfileComponent): MainChild()
        data class OrderHistoryScreen(val component: OrderHistoryComponent): MainChild()
        data class OrderHistoryAllScreen(val component: OrderHistoryAllComponent): MainChild()
        data class TrackOrderScreen(val component: TrackOrderComponent): MainChild()
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

    private val _actions = Channel<Actions>(Channel.UNLIMITED)
    override val actions = _actions.receiveAsFlow()

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
            MainConfiguration.BasketScreen -> MainComponent.MainChild.BasketScreen(
                BasketComponent(
                    componentContext = context,
                    plantsRepository = appModule.plantsRepository,
                    basketRepository = appModule.basketRepository,
                    profileRepository = appModule.profileRepository,
                    orderPlants = OrderPlantsUseCase(appModule.orderRepository),
                    onComponentEvents = { event ->
                        when(event) {
                            BasketEvent.ComponentEvents.NavigateToEditAddress -> showMessage(UiText.DynamicString("Not implemented")) // TODO: add navigation if the screen will be available on this branch
                            BasketEvent.ComponentEvents.NavigateToEditProfile -> showMessage(UiText.DynamicString("Not implemented")) // TODO: add navigation if the screen will be available on this branch
                            BasketEvent.ComponentEvents.NavigateToHome -> onEvent(MainScreenEvent.OnHomeTabClicked)
                            is BasketEvent.ComponentEvents.ShowMessage -> showMessage(event.message)
                        }
                    },
                )
            )
            MainConfiguration.FavouritesScreen -> MainComponent.MainChild.FavouritesScreen(
                FavouritesComponent(
                    componentContext = context,
                    plantsRepository = appModule.plantsRepository
                ) { plant ->
                    _state.update { it.copy(bottomNavigationVisible = false) }
                    navigation.pushNew(MainConfiguration.PlantDetailScreen(plant))
                }
            )
            MainConfiguration.HomeScreen -> MainComponent.MainChild.HomeScreen(HomeScreenComponent(
                componentContext = context,
                plantsRepository = appModule.plantsRepository,
                profileRepository = appModule.profileRepository,
                onNavigateToDetail = { plant ->
                    _state.update { it.copy(bottomNavigationVisible = false) }
                    navigation.pushNew(MainConfiguration.PlantDetailScreen(plant))
                },
                onNavigateToProfile = { user ->
                    _state.update { it.copy(bottomNavigationVisible = false) }
                    navigation.pushNew(MainConfiguration.ProfileScreen(user))
                }
            ))
            is MainConfiguration.PlantDetailScreen -> MainComponent.MainChild.PlantDetailScreen(
                PlantDetailComponent(
                    plant = config.plant,
                    componentContext =  context,
                    shareUtils = appModule.shareUtils,
                    plantsRepository = appModule.plantsRepository,
                    basketRepository = appModule.basketRepository,
                    onComponentEvent = { event ->
                        when(event) {
                            PlantDetailEvent.NavigateBack -> {
                                _state.update { it.copy(bottomNavigationVisible = true) }
                                navigation.pop()
                            }
                            PlantDetailEvent.NavigateToBasket -> {
                                navigation.pop()
                                onEvent(MainScreenEvent.OnBasketTabClicked)
                            }
                            else -> Unit
                        }
                    },
            ))
            is MainConfiguration.ProfileScreen -> MainComponent.MainChild.ProfileScreen(
                ProfileComponent(
                    user = config.user,
                    componentContext = context
                ) { event ->
                    when (event) {
                        ProfileEvent.NavigateBack -> {
                            _state.update { it.copy(bottomNavigationVisible = true) }
                            navigation.pop()
                        }
                        ProfileEvent.OnMyOrdersClick -> {
                            _state.update { it.copy(bottomNavigationVisible = false) }
                            navigation.pushNew(MainConfiguration.OrderHistoryScreen)
                        }
                        else -> Unit
                    }
                }
            )

            is MainConfiguration.OrderHistoryScreen -> MainComponent.MainChild.OrderHistoryScreen(
                OrderHistoryComponent(
                    componentContext = context,
                    plantsRepository = appModule.plantsRepository,
                    ordersRepository = appModule.orderRepository,
                    profileRepository = appModule.profileRepository
                ) { event ->
                    when(event) {
                        OrderHistoryEvents.ComponentEvents.NavigateBack -> navigation.pop()
                        is OrderHistoryEvents.ComponentEvents.ShowAllPressed -> {
                            _state.update { it.copy(bottomNavigationVisible = false) }
                            navigation.pushNew(MainConfiguration.OrderHistoryAllScreen(event.selectedType))
                        }
                        is OrderHistoryEvents.ComponentEvents.ShowMessage -> showMessage(event.message)
                        OrderHistoryEvents.ComponentEvents.StartOrderPressed -> {
                            _state.update { it.copy(bottomNavigationVisible = true) }
                            navigation.replaceAll(MainConfiguration.HomeScreen)
                        }
                        is OrderHistoryEvents.ComponentEvents.TrackOrderPressed -> {
                            _state.update { it.copy(bottomNavigationVisible = false) }
                            navigation.pushNew(MainConfiguration.TrackOrderScreen(event.order))
                        }
                    }
                }
            )

            is MainConfiguration.OrderHistoryAllScreen -> MainComponent.MainChild.OrderHistoryAllScreen(
                OrderHistoryAllComponent(
                    componentContext = context,
                    config.status,
                    ordersRepository = appModule.orderRepository,
                    plantsRepository = appModule.plantsRepository,
                    profileRepository = appModule.profileRepository
                ) { event ->
                    when (event) {
                        OrderHistoryEvents.ComponentEvents.NavigateBack -> navigation.pop()
                        is OrderHistoryEvents.ComponentEvents.ShowMessage -> showMessage(event.message)
                        else -> Unit
                    }
                }
            )

            is MainConfiguration.TrackOrderScreen -> MainComponent.MainChild.TrackOrderScreen(
                TrackOrderComponent(
                    componentContext = context,
                    order = config.order,
                    ordersRepository = appModule.orderRepository,
                    profileRepository = appModule.profileRepository
                ) { event ->
                    when(event) {
                        TrackOrderEvents.ComponentEvents.NavigateBack -> navigation.pop()
                    }
                }
            )
        }
    }

    private fun showMessage(message: UiText) {
        _actions.trySend(Actions.ShowMessageAction(Clock.System.now().epochSeconds ,message))
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
        @Serializable
        data class ProfileScreen(val user: User?): MainConfiguration()
        @Serializable
        data object OrderHistoryScreen: MainConfiguration()
        @Serializable
        data class OrderHistoryAllScreen(val status: OrderStatus): MainConfiguration()
        @Serializable
        data class TrackOrderScreen(val order: OrderItem): MainConfiguration()
    }

    sealed interface Actions {
        data class ShowMessageAction(val id: Long, val message: UiText): Actions
    }
}