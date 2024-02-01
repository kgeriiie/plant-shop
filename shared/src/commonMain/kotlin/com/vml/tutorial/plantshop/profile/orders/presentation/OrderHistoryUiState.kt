package com.vml.tutorial.plantshop.profile.orders.presentation

import com.vml.tutorial.plantshop.core.utils.exts.orFalse
import com.vml.tutorial.plantshop.profile.orders.domain.OrderStatus

data class OrderHistoryUiState(
    val items: List<OrderListItemUiState>? = null,
    val contentLoading: Boolean = false
) {
    val pendingOrders: List<OrderListItemUiState> get() { return items?.filter { it.data.status == OrderStatus.PENDING }.orEmpty() }
    val shippedOrders: List<OrderListItemUiState> get() { return items?.filter { it.data.status == OrderStatus.SHIPPED }.orEmpty() }
    val cancelledOrders: List<OrderListItemUiState> get() { return items?.filter { it.data.status == OrderStatus.CANCELLED }.orEmpty() }

    val displayEmptyMessage: Boolean get() { return items?.isEmpty().orFalse() && !contentLoading }
}