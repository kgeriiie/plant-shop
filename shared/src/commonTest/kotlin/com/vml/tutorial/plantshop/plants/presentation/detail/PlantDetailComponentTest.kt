package com.vml.tutorial.plantshop.plants.presentation.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackHandler
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.vml.tutorial.plantshop.basket.data.BasketRepository
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.utils.ShareHelper
import com.vml.tutorial.plantshop.core.utils.ShareHelperImpl
import com.vml.tutorial.plantshop.helpers.ComponentContextIml
import com.vml.tutorial.plantshop.helpers.createPlant
import com.vml.tutorial.plantshop.helpers.createPlantDetailState
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.detail.components.PlantDetailEvent
import io.mockative.Fun1
import io.mockative.Mock
import io.mockative.any
import io.mockative.classOf
import io.mockative.coEvery
import io.mockative.coVerify
import io.mockative.every
import io.mockative.mock
import io.mockative.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PlantDetailComponentTest {

    private var testPlant = createPlant()
    private var testFavouritePlantsFlow: MutableStateFlow<List<Plant>> = MutableStateFlow(listOf())

    @Mock
    private val shareHelperMock: ShareHelper = mock(classOf<ShareHelper>())
    @Mock
    private val backHandlerMock: BackHandler = mock(classOf<BackHandler>())
    @Mock
    private val instanceKeeperMock: InstanceKeeper = mock(classOf<InstanceKeeper>())
    @Mock
    private val lifecycleMock: Lifecycle = mock(classOf<Lifecycle>())
    @Mock
    private val stateKeeperMock: StateKeeper = mock(classOf<StateKeeper>())
    @Mock
    private val plantsRepositoryMock: PlantsRepository = mock(classOf<PlantsRepository>())
    @Mock
    private val basketRepositoryMock: BasketRepository = mock(classOf<BasketRepository>())
    @Mock
    private val onComponentEventMock: Fun1<PlantDetailEvent, Unit> = mock(classOf<Fun1<PlantDetailEvent, Unit>>())

    private val componentContext: ComponentContext = ComponentContextIml(backHandlerMock, instanceKeeperMock, lifecycleMock, stateKeeperMock)
    private lateinit var plantDetailComponent: PlantDetailComponent

    @BeforeTest
    fun setupTests() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())

        every { lifecycleMock.state }.returns(Lifecycle.State.CREATED)
        every { onComponentEventMock.invoke(any()) }.returns(Unit)

        testFavouritePlantsFlow = MutableStateFlow(listOf())
        every { plantsRepositoryMock.getFavorites() }.returns(testFavouritePlantsFlow)

        plantDetailComponent = PlantDetailComponent(
            plant = testPlant,
            componentContext = componentContext,
            shareHelper = shareHelperMock,
            plantsRepository = plantsRepositoryMock,
            basketRepository = basketRepositoryMock,
            onComponentEvent = { onComponentEventMock.invoke(it) }
        )
    }

    // region CHECKOUT PLANT
    @Test
    fun checkoutPlant_whenCheckoutPlantEventInvoked_thenInsertItemAndResetState() = runTest {
        // Given
        plantDetailComponent.onEvent(PlantDetailEvent.OnQuantityChanged(2))

        val states = mutableListOf<PlantDetailState>()
        val job = CoroutineScope(UnconfinedTestDispatcher(testScheduler)).launch {
            plantDetailComponent.state.toList(states)
        }

        // When
        plantDetailComponent.onEvent(PlantDetailEvent.CheckoutPlant)

        // Then
        delay(500)
        coVerify { basketRepositoryMock.insertItem(any(), any()) }.wasInvoked(exactly = 1)
        val currentState = states.last()
        assertEquals(expected = true, actual = currentState.showAddToBasketDialog, message = "Are we showing dialog?")
        assertEquals(expected = 1, actual = currentState.quantity, message = "Did quantity reset")
        job.cancel()
    }
    // endregion

    // region FAVOURITES
    @Test
    fun favourite_whenOnFavouriteClickEventInvoked_thenCallToggleFavouriteStatus() = runTest {
        // Given
        // When
        plantDetailComponent.onEvent(PlantDetailEvent.OnFavouriteClick)

        // Then
        delay(500)
        coVerify { plantsRepositoryMock.toggleFavoriteStatus(any()) }.wasInvoked(exactly = 1)
    }

    @Test
    fun favourite_whenHasFavouriteWithSameIdInPlantsRepository_thenSetAsFavourite() = runTest {
        // Given
        val states = mutableListOf<PlantDetailState>()
        val job = CoroutineScope(UnconfinedTestDispatcher(testScheduler)).launch {
            plantDetailComponent.state.toList(states)
        }
        // When
        testFavouritePlantsFlow.value = listOf(createPlant())

        // Then
        delay(500)
        val currentState = states.last()
        assertEquals(expected = true, actual = currentState.isFavourite, message = "It marked as favourite plant")
        job.cancel()
    }

    @Test
    fun favourite_whenHasFavouriteButTheIdIsDifferent_thenDoNotSetAsFavourite() = runTest {
        // Given
        val states = mutableListOf<PlantDetailState>()
        val job = CoroutineScope(UnconfinedTestDispatcher(testScheduler)).launch {
            plantDetailComponent.state.toList(states)
        }
        // When
        testFavouritePlantsFlow.value = listOf(createPlant(id = 2))

        // Then
        delay(500)
        val currentState = states.last()
        assertEquals(expected = false, actual = currentState.isFavourite, message = "It hasn't marked as favourite plant")
        job.cancel()
    }
    // endregion

    // region ON QUANTITY CHANGED
    @Test
    fun quantity_whenValueIsLargerThan1_thenUpdateState() = runTest {
        // Given
        val states = mutableListOf<PlantDetailState>()
        val job = CoroutineScope(UnconfinedTestDispatcher(testScheduler)).launch {
            plantDetailComponent.state.toList(states)
        }

        // When
        plantDetailComponent.onEvent(PlantDetailEvent.OnQuantityChanged(3))

        // Then
        delay(500)
        val currentState = states.last()
        assertEquals(expected = 3, actual = currentState.quantity, message = "Quantity set to 3")
        job.cancel()
    }

    @Test
    fun quantity_whenValueIsZero_thenUpdateState() = runTest {
        // Given
        val states = mutableListOf<PlantDetailState>()
        val job = CoroutineScope(UnconfinedTestDispatcher(testScheduler)).launch {
            plantDetailComponent.state.toList(states)
        }

        // When
        plantDetailComponent.onEvent(PlantDetailEvent.OnQuantityChanged(0))

        // Then
        delay(500)
        val currentState = states.last()
        assertNotEquals(illegal = 0, actual = currentState.quantity, message = "Quantity can not be zero")
        job.cancel()
    }
    // endregion

    // region SHARE CLICK
    @Test
    fun share_whenOnShareClickInvokes_thenCallHelper() = runTest {
        // Given
        // When
        plantDetailComponent.onEvent(PlantDetailEvent.OnShareClick(""))

        // Then
        verify { shareHelperMock.shareContent(any()) }.wasInvoked(exactly = 1)
    }
    // endregion

    // region DISMISS DIALOG CLICK
    @Test
    fun dismiss_whenDismissDialog_thenResetStateOfShowDialogFlag() = runTest {
        // Given
        val states = mutableListOf<PlantDetailState>()
        val job = CoroutineScope(UnconfinedTestDispatcher(testScheduler)).launch {
            plantDetailComponent.state.toList(states)
        }

        // When
        plantDetailComponent.onEvent(PlantDetailEvent.DismissDialog)

        // Then
        delay(500)
        val currentState = states.last()
        assertEquals(expected = false, actual = currentState.showAddToBasketDialog, message = "Hide dialog")
        job.cancel()
    }
    // endregion

    // region COMPONENT EVENTS CHECK
    @Test
    fun checkComponentCall_whenNavigateBackEventReceived() = runTest {
        // Given
        // When
        plantDetailComponent.onEvent(PlantDetailEvent.NavigateBack)
        // Then
        verify { onComponentEventMock.invoke(PlantDetailEvent.NavigateBack) }.wasInvoked(exactly = 1)
    }

    @Test
    fun checkComponentCall_whenNavigateToBasketEventReceived() = runTest {
        // Given
        // When
        plantDetailComponent.onEvent(PlantDetailEvent.NavigateToBasket)
        // Then
        verify { onComponentEventMock.invoke(PlantDetailEvent.NavigateToBasket) }.wasInvoked(exactly = 1)
    }
    // endregion
}