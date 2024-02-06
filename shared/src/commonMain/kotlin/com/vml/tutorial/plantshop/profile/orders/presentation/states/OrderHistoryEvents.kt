package com.vml.tutorial.plantshop.profile.orders.presentation.states

import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem
import com.vml.tutorial.plantshop.profile.orders.domain.OrderStatus

sealed interface OrderHistoryEvents {
    data object FetchContents: OrderHistoryEvents
    data class ShowMessage(val message: UiText): OrderHistoryEvents
    data class PrimaryButtonPressed(val order: OrderItem): OrderHistoryEvents
    data class SecondaryButtonPressed(val order: OrderItem): OrderHistoryEvents
    data class ShowAllPressed(val selectedType: OrderStatus): OrderHistoryEvents
    data object StartOrderPressed: OrderHistoryEvents
    data object NavigateBack: OrderHistoryEvents
    data class ConfirmDialogDismissed(val action: OrderHistoryConfirmAction, val confirmed: Boolean): OrderHistoryEvents
}