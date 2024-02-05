package com.vml.tutorial.plantshop.profile.orders.presentation.states

import com.vml.tutorial.plantshop.core.utils.exts.orFalse
import com.vml.tutorial.plantshop.profile.orders.domain.OrderStatus

data class OrderHistoryUiState(
    val items: List<OrderListItemUiState>? = null,
    val contentLoading: Boolean = false,
    val confirmAction: OrderHistoryConfirmAction? = null
) {
    val pendingOrders: List<OrderListItemUiState> get() { return items?.filter { it.data.status == OrderStatus.PENDING }?.sortedByDescending { it.data.createdAt }.orEmpty() }
    val shippedOrders: List<OrderListItemUiState> get() { return items?.filter { it.data.status == OrderStatus.SHIPPED }?.sortedByDescending { it.data.updatedAt }.orEmpty() }
    val cancelledOrders: List<OrderListItemUiState> get() { return items?.filter { it.data.status == OrderStatus.CANCELLED }?.sortedByDescending { it.data.updatedAt }.orEmpty() }

    val displayEmptyMessage: Boolean get() { return items?.isEmpty().orFalse() && !contentLoading }
}