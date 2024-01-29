package com.vml.tutorial.plantshop.profile.orders.presentation.states

import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem

sealed class OrderHistoryConfirmAction(val item: OrderItem, val message: UiText) {
    data class Cancellation(private val orderItem: OrderItem): OrderHistoryConfirmAction(orderItem, UiText.StringRes(MR.strings.orders_confirm_cancellation_message_text))
    data class Reorder(private val orderItem: OrderItem): OrderHistoryConfirmAction(orderItem, UiText.StringRes(MR.strings.orders_confirm_reorder_message_text))
}