package com.vml.tutorial.plantshop.profile.orders.presentation

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.core.presentation.UiText

import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderHistoryEvents
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderHistoryUiState
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderListItemUiState
import com.vml.tutorial.plantshop.profile.orders.data.OrdersRepository
import com.vml.tutorial.plantshop.profile.orders.data.usecase.OrderPlantsUseCase
import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem
import com.vml.tutorial.plantshop.profile.orders.domain.OrderStatus
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderHistoryCommonUiState
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderHistoryConfirmAction
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class OrderHistoryComponent(
    componentContext: ComponentContext,
    internal val ordersRepository: OrdersRepository,
    internal val plantsRepository: PlantsRepository,
    private val orderPlants: OrderPlantsUseCase = OrderPlantsUseCase(ordersRepository),
    private val onComponentEvent: (event: OrderHistoryEvents) -> Unit
): ComponentContext by componentContext  {
    internal val commonUiState: MutableStateFlow<OrderHistoryCommonUiState> = MutableStateFlow(OrderHistoryCommonUiState())

    private val _uiState: MutableStateFlow<OrderHistoryUiState> = MutableStateFlow(OrderHistoryUiState())
    val uiState: StateFlow<OrderHistoryUiState> = commonUiState.combine(_uiState) { common, ui ->
        ui.copy(commonState = common)
    }.stateIn(componentCoroutineScope(), SharingStarted.WhileSubscribed(), _uiState.value)

    open fun fetchOrderHistory() {
        componentCoroutineScope().launch {
            showLoader()
            val items = listOf(OrderStatus.PENDING, OrderStatus.SHIPPED, OrderStatus.CANCELLED).map { status ->
                componentCoroutineScope().async {
                    ordersRepository.getOrders(status = status, limit = 3)
                }
            }.awaitAll().flatten()

            _uiState.update {
                it.copy(
                    items = items.map { order ->
                        OrderListItemUiState(
                            data = order,
                            plantsMap = buildMap {
                                order.plantIds.forEach { plantId ->
                                    if (!this.containsKey(plantId)) {
                                        plantsRepository.getPlant(plantId)
                                            ?.let { plant -> this.put(plant.id, plant) }
                                    }
                                }
                            }
                        )
                    }
                )
            }.also {
                hideLoader()
            }
        }
    }

    open fun getOrderItemBy(orderId: String): OrderListItemUiState? {
        return uiState.value.items?.firstOrNull { it.data.id == orderId }
    }

    private fun cancelOrder(orderId: String) {
        showLoader()
        componentCoroutineScope().launch {
            ordersRepository.cancelOrder(orderId).also {
                fetchOrderHistory()
                onComponentEvent(OrderHistoryEvents.ShowMessage(UiText.StringRes(MR.strings.orders_cancelled_message_text)))
            }
        }
    }

    private fun reOrder(orderId: String) {
        showLoader()
        getOrderItemBy(orderId)?.let { item ->
            componentCoroutineScope().launch {
                if (orderPlants(item.allPlants)) {
                    onComponentEvent(OrderHistoryEvents.ShowMessage(UiText.StringRes(MR.strings.orders_reordered_message_text)))
                    fetchOrderHistory()
                } else {
                    onComponentEvent(OrderHistoryEvents.ShowMessage(UiText.StringRes(MR.strings.orders_fail_to_reordered_message_text)))
                    hideLoader()
                }
            }
        }?: run {
            onComponentEvent(OrderHistoryEvents.ShowMessage(UiText.StringRes(MR.strings.orders_fail_to_reordered_message_text)))
            hideLoader()
        }
    }

    fun onEvent(event: OrderHistoryEvents) {
        when(event) {
            is OrderHistoryEvents.FetchContents -> fetchOrderHistory()
            is OrderHistoryEvents.PrimaryButtonPressed -> when(event.order.status) {
                OrderStatus.PENDING -> commonUiState.update { it.copy(confirmAction = OrderHistoryConfirmAction.Cancellation(event.order)) }
                OrderStatus.SHIPPED -> Unit // TODO: implement feature
                OrderStatus.CANCELLED -> Unit // TODO: implement feature
            }
            is OrderHistoryEvents.SecondaryButtonPressed -> when(event.order.status) {
                OrderStatus.PENDING -> Unit // TODO: implement feature
                OrderStatus.SHIPPED -> Unit // TODO: implement feature
                OrderStatus.CANCELLED -> commonUiState.update { it.copy(confirmAction = OrderHistoryConfirmAction.Reorder(event.order)) }
            }
            is OrderHistoryEvents.ConfirmDialogDismissed -> {
                commonUiState.update { it.copy(confirmAction = null) }
                if (event.confirmed) {
                    onActionConfirmed(event.action)
                }
            }
            else -> onComponentEvent.invoke(event)
        }
    }

    private fun onActionConfirmed(action: OrderHistoryConfirmAction) {
        when(action) {
            is OrderHistoryConfirmAction.Cancellation -> cancelOrder(action.item.id)
            is OrderHistoryConfirmAction.Reorder -> reOrder(action.item.id)
        }
    }

    internal open fun showLoader() {
        commonUiState.update { it.copy(contentLoading = true) }
    }

    internal open fun hideLoader() {
        commonUiState.update { it.copy(contentLoading = false) }
    }
}
