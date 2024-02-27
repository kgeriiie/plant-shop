package com.vml.tutorial.plantshop.basket.presentation.components

import com.vml.tutorial.plantshop.core.presentation.UiText

sealed interface BasketEvent {
    data class OnQuantityChanged(val plantId: Int, val value: Int): BasketEvent
    data object Checkout: BasketEvent
    data object DismissErrorDialog: BasketEvent

    sealed interface ComponentEvents: BasketEvent {
        data class ShowMessage(val message: UiText): ComponentEvents
        data object NavigateToHome: ComponentEvents
        data object NavigateToEditAddress: ComponentEvents
        data object NavigateToEditProfile: ComponentEvents
    }
}