package com.vml.tutorial.plantshop.profile.orders.presentation.states

import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem
import com.vml.tutorial.plantshop.profile.orders.domain.OrderStatus
import com.vml.tutorial.plantshop.rate.domain.Rating

sealed interface OrderHistoryEvents {
    data object FetchContents: OrderHistoryEvents
    data class PrimaryButtonPressed(val order: OrderItem): OrderHistoryEvents
    data class SecondaryButtonPressed(val order: OrderItem): OrderHistoryEvents
    data class ConfirmDialogDismissed(val action: OrderHistoryConfirmAction, val confirmed: Boolean): OrderHistoryEvents
    data class OnRateSubmitted(val rating: Rating): OrderHistoryEvents
    data object DismissRatingDialog: OrderHistoryEvents

    sealed interface ComponentEvents: OrderHistoryEvents {
        data object NavigateBack: ComponentEvents
        data class ShowMessage(val message: UiText): ComponentEvents
        data class ShowAllPressed(val selectedType: OrderStatus): ComponentEvents
        data class TrackOrderPressed(val order: OrderItem): ComponentEvents
        data object StartOrderPressed: ComponentEvents
    }
}