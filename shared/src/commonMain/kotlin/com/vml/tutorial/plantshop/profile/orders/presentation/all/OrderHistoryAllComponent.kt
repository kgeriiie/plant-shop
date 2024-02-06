package com.vml.tutorial.plantshop.profile.orders.presentation.all

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.profile.orders.data.OrdersRepository
import com.vml.tutorial.plantshop.profile.orders.data.usecase.OrderPlantsUseCase
import com.vml.tutorial.plantshop.profile.orders.domain.OrderStatus
import com.vml.tutorial.plantshop.profile.orders.presentation.OrderHistoryComponent
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderHistoryEvents
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderListItemUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderHistoryAllComponent(
    componentContext: ComponentContext,
    private val status: OrderStatus,
    ordersRepository: OrdersRepository,
    plantsRepository: PlantsRepository,
    orderPlants: OrderPlantsUseCase = OrderPlantsUseCase(ordersRepository),
    onComponentEvent: (event: OrderHistoryEvents) -> Unit
): OrderHistoryComponent(componentContext, ordersRepository, plantsRepository, orderPlants, onComponentEvent) {

    private val _state: MutableStateFlow<OrderHistoryAllUiState> = MutableStateFlow(OrderHistoryAllUiState(status))
    val state: StateFlow<OrderHistoryAllUiState> = commonUiState.combine(_state) { common, uiState ->
        uiState.copy(commonState = common)
    }.stateIn(componentCoroutineScope(), SharingStarted.WhileSubscribed(), _state.value)

    override fun fetchOrderHistory() {
        componentCoroutineScope().launch {
            showLoader()
            _state.update { state ->
                state.copy(items = ordersRepository.getOrders(status)
                        .sortedByDescending { it.updatedAt }
                        .mapOrderItemsToState())
            }.also {
                hideLoader()
            }
        }
    }

    override fun getOrderItemBy(orderId: String): OrderListItemUiState? {
        return state.value.items?.firstOrNull { it.data.id == orderId }
    }
}