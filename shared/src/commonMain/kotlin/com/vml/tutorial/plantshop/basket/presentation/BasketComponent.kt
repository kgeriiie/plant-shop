package com.vml.tutorial.plantshop.basket.presentation

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.basket.data.BasketRepository
import com.vml.tutorial.plantshop.basket.presentation.components.BasketEvent
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.Plant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BasketComponent(
    componentContext: ComponentContext,
    private val plantsRepository: PlantsRepository,
    private val basketRepository: BasketRepository,
    private val onNavigateToHome: () -> Unit,
    private val onShowMessage: (message: UiText) -> Unit,
): ComponentContext by componentContext {

    private val plantsFlow: MutableStateFlow<List<Plant>> = MutableStateFlow(listOf())
    val state: StateFlow<BasketScreenState> = plantsFlow.combine(basketRepository.basketItemsFlow) { plants, basket ->
        BasketScreenState(
            items = basket.mapNotNull { item ->
                plants.firstOrNull { it.id == item.plantId }?.let { plant ->
                    BasketItemState(plant, item.quantity)
                }
            }
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
        componentCoroutineScope().launch {
            basketRepository.deleteAll()
            onShowMessage.invoke(UiText.StringRes(MR.strings.basket_checkout_message))
        }
    }
}
