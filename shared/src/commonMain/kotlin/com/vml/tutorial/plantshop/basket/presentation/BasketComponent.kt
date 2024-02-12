package com.vml.tutorial.plantshop.basket.presentation

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.basket.data.BasketRepository
import com.vml.tutorial.plantshop.basket.presentation.components.BasketEvent
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.PlantCategory
import com.vml.tutorial.plantshop.profile.orders.data.OrdersRepository
import com.vml.tutorial.plantshop.profile.orders.data.usecase.OrderPlantsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BasketComponent(
    componentContext: ComponentContext,
    private val plantsRepository: PlantsRepository,
    private val basketRepository: BasketRepository,
    private val orderPlants: OrderPlantsUseCase,
    private val onNavigateToHome: () -> Unit,
    private val onShowMessage: (message: UiText) -> Unit,
): ComponentContext by componentContext {

    private val plantsFlow: MutableStateFlow<List<Plant>> = MutableStateFlow(listOf())
    private val uiActionStateFlow: MutableStateFlow<BasketScreenUiAction> = MutableStateFlow(BasketScreenUiAction())
    val state: StateFlow<BasketScreenState> = combine(plantsFlow, basketRepository.basketItemsFlow, uiActionStateFlow) { plants, basket, uiAction ->
        BasketScreenState(
            items = basket.mapNotNull { item ->
                plants.firstOrNull { it.id == item.plantId }?.let { plant ->
                    BasketItemState(plant, item.quantity)
                }
            },
            checkoutInProgress = uiAction.checkoutInProgress
        )
    }.stateIn(componentCoroutineScope(), SharingStarted.Lazily, BasketScreenState())

    init {
        componentCoroutineScope().launch {
            plantsRepository.getPlants()?.let { plantsFlow.emit(it) }
            basketRepository.getBasketItems()
        }
    }

    fun onEvent(event: BasketEvent) {
        when(event) {
            BasketEvent.Checkout -> checkout()
            BasketEvent.ExplorePlants -> onNavigateToHome()
            is BasketEvent.OnQuantityChanged -> {
                componentCoroutineScope().launch {
                    if (event.value > 0) {
                        basketRepository.updateItemQuantity(event.plantId, event.value)
                    } else {
                        basketRepository.deleteItem(event.plantId)
                    }
                }
            }
        }
    }

    private fun checkout() {
        uiActionStateFlow.update { it.copy(checkoutInProgress = true) }
        // add all plant to list depending on it's quantity
        val orders = state.value.items?.map { item ->
            buildList {
                for (i in 0 until item.quantity) {
                    add(item.plant)
                }
            }
        }?.flatten()?: return

        componentCoroutineScope().launch {
            orderPlants(orders, state.value.discount).also { success ->
                if (success) {
                    basketRepository.deleteAll()
                    uiActionStateFlow.update { it.copy(checkoutInProgress = false) }
                    onShowMessage.invoke(UiText.StringRes(MR.strings.basket_checkout_message))
                } else {
                    onShowMessage.invoke(UiText.StringRes(MR.strings.basket_fail_to_checkout_message))
                }
            }
        }
    }
}
