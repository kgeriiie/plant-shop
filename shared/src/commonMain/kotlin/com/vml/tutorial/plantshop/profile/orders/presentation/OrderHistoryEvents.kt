package com.vml.tutorial.plantshop.profile.orders.presentation

import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem
import com.vml.tutorial.plantshop.profile.orders.domain.OrderStatus

sealed interface OrderHistoryEvents {
    data class PrimaryButtonPressed(val order: OrderItem): OrderHistoryEvents
    data class SecondaryButtonPressed(val order: OrderItem): OrderHistoryEvents
    data class ShowAllPressed(val selectedType: OrderStatus): OrderHistoryEvents
    data object StartOrderPressed: OrderHistoryEvents
    data object NavigateBack: OrderHistoryEvents
}