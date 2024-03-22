package com.vml.tutorial.plantshop.main.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import com.vml.tutorial.plantshop.basket.presentation.BasketComponent
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.di.AppModule
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.detail.PlantDetailComponent
import com.vml.tutorial.plantshop.plants.presentation.detail.components.PlantDetailEvent
import com.vml.tutorial.plantshop.plants.presentation.favourites.FavouritesComponent
import com.vml.tutorial.plantshop.plants.presentation.home.HomeScreenComponent
import com.vml.tutorial.plantshop.profilePreferences.domain.User
import com.vml.tutorial.plantshop.profilePreferences.presentation.editAddress.EditAddressComponent
import com.vml.tutorial.plantshop.profilePreferences.presentation.editAddress.components.EditAddressEvent
import com.vml.tutorial.plantshop.profilePreferences.presentation.editPersonalInfo.EditProfileComponent
import com.vml.tutorial.plantshop.profilePreferences.presentation.editPersonalInfo.components.EditProfileEvent
import com.vml.tutorial.plantshop.profilePreferences.presentation.paymentMethod.PaymentMethodComponent
import com.vml.tutorial.plantshop.profilePreferences.presentation.preferences.PreferencesComponent
import com.vml.tutorial.plantshop.profilePreferences.presentation.preferences.components.PreferencesEvent
import com.vml.tutorial.plantshop.profilePreferences.presentation.profile.ProfileComponent
import com.vml.tutorial.plantshop.profilePreferences.presentation.profile.components.ProfileEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable

interface MainComponent {
    val childStack: Value<ChildStack<*, MainChild>>
    var state: StateFlow<MainScreenState>
    val actions: Flow<DefaultMainComponent.Actions>

    fun onEvent(event: MainScreenEvent)
    sealed class MainChild {
        data class HomeScreen(val component: HomeScreenComponent) : MainChild()
        data class FavouritesScreen(val component: FavouritesComponent) : MainChild()
        data class BasketScreen(val component: BasketComponent) : MainChild()
        data class PlantDetailScreen(val component: PlantDetailComponent) : MainChild()
        data class ProfileScreen(val component: ProfileComponent) : MainChild()
        data class PreferencesScreen(val component: PreferencesComponent) : MainChild()
        data class EditAddressScreen(val component: EditAddressComponent) : MainChild()
        data class EditPersonalInfoScreen(val component: EditProfileComponent) : MainChild()
        data class PaymentMethodScreen(val component: PaymentMethodComponent) : MainChild()
    }
}

class DefaultMainComponent(
    componentContext: ComponentContext,
    private val appModule: AppModule,
    private val onLogOut: () -> Unit
) : MainComponent, ComponentContext by componentContext {

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
        when (event) {
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
                    onNavigateToHome = { onEvent(MainScreenEvent.OnHomeTabClicked) },
                    onShowMessage = ::showMessage
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
                    componentContext = context,
                    shareHelper = appModule.shareHelper,
                    plantsRepository = appModule.plantsRepository,
                    basketRepository = appModule.basketRepository,
                    onComponentEvent = { event ->
                        when (event) {
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
                    }
                )
            )

            is MainConfiguration.ProfileScreen -> MainComponent.MainChild.ProfileScreen(
                ProfileComponent(
                    user = config.user,
                    authRepository = appModule.authRepository,
                    profileRepository = appModule.profileRepository,
                    componentContext = context,
                    onShowMessage = ::showMessage,
                    onLogout = onLogOut,
                    onComponentEvent =
                    { event ->
                        when (event) {
                            ProfileEvent.NavigateBack -> {
                                _state.update { it.copy(bottomNavigationVisible = true) }
                                navigation.pop()
                            }
                            ProfileEvent.OnPreferencesClicked -> {
                                _state.update { it.copy(bottomNavigationVisible = false) }
                                navigation.pushNew(MainConfiguration.PreferencesScreen)
                            }

                            else -> Unit
                        }
                    }
                )
            )

            is MainConfiguration.PreferencesScreen -> MainComponent.MainChild.PreferencesScreen(
                PreferencesComponent(
                    componentContext = context,
                    authRepository = appModule.authRepository,
                    profileRepository = appModule.profileRepository,
                    onShowMessage = ::showMessage,
                    onLogout = onLogOut
                ) { event, user ->
                    when (event) {
                        PreferencesEvent.NavigateBack -> navigation.pop()
                        PreferencesEvent.OnEditAddressClicked -> navigation.pushNew(
                            MainConfiguration.EditAddressScreen(user)
                        )

                        PreferencesEvent.OnEditPersonalDataClicked -> navigation.pushNew(
                            MainConfiguration.EditPersonalInfoScreen(user)
                        )

                        PreferencesEvent.OnPaymentMethodClicked -> navigation.pushNew(
                            MainConfiguration.PaymentMethodScreen(user)
                        )

                        else -> Unit
                    }
                }
            )

            is MainConfiguration.EditAddressScreen -> MainComponent.MainChild.EditAddressScreen(
                EditAddressComponent(
                    user = config.user,
                    componentContext = context,
                    profileRepository = appModule.profileRepository,
                    onShowMessage = ::showMessage,
                    onComponentEvent = { event ->
                        when (event) {
                            EditAddressEvent.NavigateBack -> navigation.pop() //TODO: Use a simple callback function instead of passing the whole event
                            else -> Unit
                        }
                    })
            )

            is MainConfiguration.EditPersonalInfoScreen -> MainComponent.MainChild.EditPersonalInfoScreen(
                EditProfileComponent(
                    user = config.user,
                    componentContext = context,
                    profileRepository = appModule.profileRepository,
                    onShowMessage = ::showMessage,
                    onComponentEvent = { event ->
                        when (event) {
                            EditProfileEvent.NavigateBack -> navigation.pop() //TODO: Use a simple callback function instead of passing the whole event
                            else -> Unit
                        }
                    })
            )

            is MainConfiguration.PaymentMethodScreen -> MainComponent.MainChild.PaymentMethodScreen(
                PaymentMethodComponent(
                    user = config.user,
                    componentContext = context,
                    profileRepository = appModule.profileRepository,
                    onShowMessage = ::showMessage,
                    onNavigateBack = { navigation.pop() })
            )
        }
    }

    private fun showMessage(message: UiText) {
        _actions.trySend(Actions.ShowMessageAction(message))
    }

    @Serializable
    sealed class MainConfiguration {
        @Serializable
        data object HomeScreen : MainConfiguration()

        @Serializable
        data object FavouritesScreen : MainConfiguration()

        @Serializable
        data object BasketScreen : MainConfiguration()

        @Serializable
        data class PlantDetailScreen(val plant: Plant) : MainConfiguration()

        @Serializable
        data class ProfileScreen(val user: User?) : MainConfiguration()

        @Serializable
        data object PreferencesScreen : MainConfiguration()

        @Serializable
        data class EditAddressScreen(val user: User?) : MainConfiguration()

        @Serializable
        data class EditPersonalInfoScreen(val user: User?) : MainConfiguration()

        @Serializable
        data class PaymentMethodScreen(val user: User?) : MainConfiguration()
    }

    data class MainUiState(
        val bottomNavigationVisible: Boolean = true
    )

    sealed interface Actions {
        data class ShowMessageAction(val message: UiText) : Actions
    }
}
