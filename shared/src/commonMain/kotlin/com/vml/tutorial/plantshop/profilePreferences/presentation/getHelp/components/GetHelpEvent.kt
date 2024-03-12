package com.vml.tutorial.plantshop.profilePreferences.presentation.getHelp.components

import com.vml.tutorial.plantshop.profilePreferences.presentation.paymentMethod.components.PaymentMethodEvent

sealed interface GetHelpEvent {
    data object NavigateBack: GetHelpEvent
    data object CallClicked : GetHelpEvent
}