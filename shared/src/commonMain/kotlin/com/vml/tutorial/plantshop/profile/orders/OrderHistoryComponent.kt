package com.vml.tutorial.plantshop.profile.orders

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.MR.strings.orders_item_cancel_order_text
import com.vml.tutorial.plantshop.MR.strings.orders_item_order_id_text
import com.vml.tutorial.plantshop.MR.strings.orders_item_rate_order_text
import com.vml.tutorial.plantshop.MR.strings.orders_item_rate_us_text
import com.vml.tutorial.plantshop.MR.strings.orders_item_re_order_text
import com.vml.tutorial.plantshop.MR.strings.orders_item_track_order_text
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.core.utils.exts.orFalse
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.profile.orders.data.OrdersRepository
import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem
import com.vml.tutorial.plantshop.profile.orders.domain.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderHistoryComponent(
    componentContext: ComponentContext,
    private val ordersRepository: OrdersRepository,
    private val plantsRepository: PlantsRepository
): ComponentContext by componentContext  {
    private val _uiState: MutableStateFlow<OrderHistoryUiState> = MutableStateFlow(OrderHistoryUiState())
    val uiState: StateFlow<OrderHistoryUiState> = _uiState.asStateFlow()

    init {
        fetchOrderHistory()
    }

    private fun fetchOrderHistory() {
        componentCoroutineScope().launch {
            _uiState.update { it.copy(contentLoading = true) }

            val orders = ordersRepository.getOrders()
            _uiState.update {
                it.copy(
                    contentLoading = false,
                    items = orders.map { order ->
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
            }
        }
    }

    fun onEvent(event: OrderHistoryEvents) {
        // TODO: handle events.
    }
}

data class OrderHistoryUiState(
    val items: List<OrderListItemUiState>? = null,
    val contentLoading: Boolean = false
) {
    val pendingOrders: List<OrderListItemUiState> get() { return items?.filter { it.data.status == OrderStatus.PENDING }.orEmpty() }
    val shippedOrders: List<OrderListItemUiState> get() { return items?.filter { it.data.status == OrderStatus.SHIPPED }.orEmpty() }
    val cancelledOrders: List<OrderListItemUiState> get() { return items?.filter { it.data.status == OrderStatus.CANCELLED }.orEmpty() }

    val displayEmptyMessage: Boolean get() { return items?.isEmpty().orFalse() && !contentLoading }
}

data class OrderListItemUiState(
    val data: OrderItem,
    private val plantsMap: Map<Int, Plant>
) {
    val orderId: UiText get() {
        return UiText.StringRes(orders_item_order_id_text, listOf(data.orderId))
    }
    val title: UiText get() {
        return UiText.DynamicString(plantsMap.values.joinToString { it.name })
    }

    val subTitle: UiText get() {
        return data.getMessage()
    }

    val totalPrice: UiText get() {
        return UiText.DynamicString("${data.currency}${data.totalPrice}")
    }

    val plants: List<Plant> get() {
        return plantsMap.values.toList()
    }

    val primaryButtonTitle: UiText get() {
        return when(data.status) {
            OrderStatus.PENDING -> UiText.StringRes(orders_item_cancel_order_text)
            OrderStatus.SHIPPED -> UiText.StringRes(orders_item_rate_order_text)
            OrderStatus.CANCELLED -> UiText.StringRes(orders_item_rate_us_text)
        }
    }

    val secondaryButtonTitle: UiText get() {
        return when(data.status) {
            OrderStatus.PENDING -> UiText.StringRes(orders_item_track_order_text)
            OrderStatus.SHIPPED -> UiText.StringRes(orders_item_re_order_text)
            OrderStatus.CANCELLED -> UiText.StringRes(orders_item_re_order_text)
        }
    }
}

sealed interface OrderHistoryEvents {
    data class PrimaryButtonPressed(val order: OrderItem): OrderHistoryEvents
    data class SecondaryButtonPressed(val order: OrderItem): OrderHistoryEvents
    data class ShowAllPressed(val selectedType: OrderStatus): OrderHistoryEvents
    data object StartOrderPressed: OrderHistoryEvents
}