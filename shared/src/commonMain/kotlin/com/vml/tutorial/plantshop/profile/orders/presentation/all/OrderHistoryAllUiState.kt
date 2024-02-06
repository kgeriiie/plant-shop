package com.vml.tutorial.plantshop.profile.orders.presentation.all

import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.profile.orders.domain.OrderStatus
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderHistoryCommonUiState
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderHistoryConfirmAction
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderListItemUiState

data class OrderHistoryAllUiState(
    val status: OrderStatus,
    val items: List<OrderListItemUiState>? = null,
    val commonState: OrderHistoryCommonUiState = OrderHistoryCommonUiState()
) {
    val screenTitle: UiText get() {
        return when (status) {
            OrderStatus.PENDING -> UiText.StringRes(MR.strings.orders_active_orders_title_text)
            OrderStatus.SHIPPED -> UiText.StringRes(MR.strings.orders_completed_orders_title_text)
            OrderStatus.CANCELLED -> UiText.StringRes(MR.strings.orders_cancelled_orders_title_text)
        }
    }
}