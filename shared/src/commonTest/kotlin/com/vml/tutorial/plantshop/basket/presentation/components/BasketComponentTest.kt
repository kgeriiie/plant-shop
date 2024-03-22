package com.vml.tutorial.plantshop.basket.presentation.components

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackHandler
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.basket.data.BasketRepository
import com.vml.tutorial.plantshop.basket.domain.BasketItem
import com.vml.tutorial.plantshop.basket.presentation.BasketComponent
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.helpers.ComponentContextIml
import io.mockative.Fun0
import io.mockative.Fun1
import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.coVerify
import io.mockative.every
import io.mockative.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest

class BasketComponentTest {
    @Mock
    private val plantRepositoryMock: PlantsRepository = mock(classOf<PlantsRepository>())
    @Mock
    private val basketRepositoryMock: BasketRepository = mock(classOf<BasketRepository>())
    @Mock
    private val backHandlerMock: BackHandler = mock(classOf<BackHandler>())
    @Mock
    private val instanceKeeperMock: InstanceKeeper = mock(classOf<InstanceKeeper>())
    @Mock
    private val lifecycleMock: Lifecycle = mock(classOf<Lifecycle>())
    @Mock
    private val stateKeeperMock: StateKeeper = mock(classOf<StateKeeper>())
    @Mock
    private val onNavigateHomeMock: Fun0<Unit> = mock(classOf<Fun0<Unit>>())
    @Mock
    private val onShowMessageMock: Fun1<UiText, Unit> = mock(classOf<Fun1<UiText, Unit>>())

    private val componentContext: ComponentContext = ComponentContextIml(backHandlerMock, instanceKeeperMock, lifecycleMock, stateKeeperMock)
    private lateinit var basketComponent: BasketComponent

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setupTests() = runTest{
        val itemsFlow = MutableStateFlow(listOf<BasketItem>()).asStateFlow()
        every { basketRepositoryMock.basketItemsFlow }.returns(itemsFlow)
        every { lifecycleMock.state }.returns(Lifecycle.State.CREATED)
        coEvery { plantRepositoryMock.getPlants() }.returns(listOf())
        coEvery { basketRepositoryMock.getBasketItems() }.returns(listOf())

        Dispatchers.setMain(StandardTestDispatcher())

        basketComponent = BasketComponent(
            componentContext = componentContext,
            plantsRepository = plantRepositoryMock,
            basketRepository = basketRepositoryMock,
            onNavigateToHome = { onNavigateHomeMock.invoke() } ,
            onShowMessage = { onShowMessageMock.invoke(it) }
        )
    }


    // region CHECKOUT TESTS
    @Test
    fun checkout_whenCheckoutEventReceived_thenBasketRepositoryDeleteAllCalled() = runTest {
        // Given
        every { onShowMessageMock.invoke(any()) }.returns(Unit)
        // When
        basketComponent.onEvent(BasketEvent.Checkout)
        // Then
        delay(DELAY_BEFORE_VERIFICATION)
        coVerify { basketRepositoryMock.deleteAll() }.wasInvoked(exactly = 1)
        coVerify { onShowMessageMock.invoke(any()) }.wasInvoked(exactly = 1)

        println("checkout_whenCheckoutEventReceived_thenBasketRepositoryDeleteAllCalled")
    }

    @Test
    fun checkout_whenCheckoutEventReceived_thenMessageWasShown() = runTest {
        // Given
        every { onShowMessageMock.invoke(any()) }.returns(Unit)
        // When
        basketComponent.onEvent(BasketEvent.Checkout)
        // Then
        delay(DELAY_BEFORE_VERIFICATION)
        coVerify { onShowMessageMock.invoke(UiText.StringRes(MR.strings.basket_checkout_message)) }.wasInvoked(exactly = 1)

        println("checkout_whenCheckoutEventReceived_thenBasketRepositoryDeleteAllCalled")
    }
    // endregion

    // region EXPLORE PLANTS
    @Test
    fun explorePlants_whenExplorePlantsEventReceived_thenNavigateToHomeCalled() = runTest {
        // Given
        every { onNavigateHomeMock.invoke() }.returns(Unit)
        // When
        basketComponent.onEvent(BasketEvent.ExplorePlants)
        // Then
        delay(DELAY_BEFORE_VERIFICATION)
        coVerify { onNavigateHomeMock.invoke() }.wasInvoked(exactly = 1)

        println("explorePlants_whenExplorePlantsEventReceived_thenNavigateToHomeCalled")
    }
    // endregion

    // region CHANGE QUANTITY
    @Test
    fun changeQuantity_whenValueNotZero_thenUpdatePlantQuantity() = runTest {
        // Given
        val updateValue = 1
        val plantId = 1
        coEvery { basketRepositoryMock.updateItemQuantity(any(), any()) }.returns(Unit)

        // When
        basketComponent.onEvent(BasketEvent.OnQuantityChanged(plantId, updateValue))

        // Then
        delay(DELAY_BEFORE_VERIFICATION)
        coVerify { basketRepositoryMock.updateItemQuantity(plantId, updateValue) }.wasInvoked(exactly = 1)
        coVerify { basketRepositoryMock.deleteItem(plantId) }.wasNotInvoked()

        println("changeQuantity_whenValueNotZero_thenUpdatePlantQuantity")
    }

    @Test
    fun changeQuantity_whenValueZero_thenDeletePlantFromBasket() = runTest {
        // Given
        val updateValue = 0
        val plantId = 1
        coEvery { basketRepositoryMock.updateItemQuantity(any(), any()) }.returns(Unit)

        // When
        basketComponent.onEvent(BasketEvent.OnQuantityChanged(plantId, updateValue))

        // Then
        delay(DELAY_BEFORE_VERIFICATION)
        coVerify { basketRepositoryMock.updateItemQuantity(plantId, updateValue) }.wasNotInvoked()
        coVerify { basketRepositoryMock.deleteItem(plantId) }.wasInvoked(exactly = 1)

        println("changeQuantity_whenValueZero_thenDeletePlantFromBasket")
    }
    // endregion

    companion object {
        const val DELAY_BEFORE_VERIFICATION = 500L
    }
}