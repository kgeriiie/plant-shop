package com.vml.tutorial.plantshop.basket.presentation

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.basket.data.BasketRepository
import com.vml.tutorial.plantshop.basket.presentation.components.BasketEvent
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.core.utils.exts.orFalse
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.profile.data.ProfileRepository
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
    private val profileRepository: ProfileRepository,
    private val orderPlants: OrderPlantsUseCase,
    private val onComponentEvents: (BasketEvent.ComponentEvents) -> Unit,
): ComponentContext by componentContext {
    private val plantsFlow: MutableStateFlow<List<Plant>> = MutableStateFlow(listOf())
    private val plantItemsState: StateFlow<List<BasketItemState>> = plantsFlow.combine(basketRepository.basketItemsFlow) { plants, basket ->
        basket.mapNotNull { item ->
            plants.firstOrNull { it.id == item.plantId }?.let { plant ->
                BasketItemState(plant, item.quantity)
            }
        }
    }.stateIn(componentCoroutineScope(), SharingStarted.Lazily, listOf())

    private val uiActionStateFlow: MutableStateFlow<BasketScreenUiAction> = MutableStateFlow(BasketScreenUiAction())
    val state: StateFlow<BasketScreenState> = combine(plantItemsState, uiActionStateFlow) { items, uiAction ->
        BasketScreenState(
            items = items,
            checkoutInProgress = uiAction.checkoutInProgress,
            error = uiAction.error
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
            is BasketEvent.OnQuantityChanged -> {
                componentCoroutineScope().launch {
                    if (event.value > 0) {
                        basketRepository.updateItemQuantity(event.plantId, event.value)
                    } else {
                        basketRepository.deleteItem(event.plantId)
                    }
                }
            }
            BasketEvent.DismissErrorDialog -> uiActionStateFlow.update {it.copy(error = null) }
            is BasketEvent.ComponentEvents -> onComponentEvents(event)
        }
    }

    private fun checkout() {
        componentCoroutineScope().launch {
            if (!profileRepository.getUser()?.address?.isFilled().orFalse()) {
                uiActionStateFlow.update {it.copy(error = BasketError.AddressMissingError) }
                return@launch
            }

            if (profileRepository.getUser()?.phoneNumber.isNullOrEmpty()) {
                uiActionStateFlow.update {it.copy(error = BasketError.PhoneNumberMissingError) }
                return@launch
            }

            val orders = collectUserOrder()?: return@launch
            uiActionStateFlow.update { it.copy(checkoutInProgress = true) }

            orderPlants(orders, state.value.discount).also { success ->
                if (success) {
                    basketRepository.deleteAll()
                    uiActionStateFlow.update { it.copy(checkoutInProgress = false) }
                    onComponentEvents(BasketEvent.ComponentEvents.ShowMessage(UiText.StringRes(MR.strings.basket_checkout_message)))
                } else {
                    uiActionStateFlow.update {it.copy(error = BasketError.DefaultError(UiText.StringRes(MR.strings.basket_fail_to_checkout_message))) }
                }
            }
        }
    }

    private fun collectUserOrder(): List<Plant>? {
        return state.value.items?.map { item ->
            // repeat plant n times (n=quantity), to get all quantity from basket.
            (0 until item.quantity).map { item.plant }
        }?.flatten()
    }
}
