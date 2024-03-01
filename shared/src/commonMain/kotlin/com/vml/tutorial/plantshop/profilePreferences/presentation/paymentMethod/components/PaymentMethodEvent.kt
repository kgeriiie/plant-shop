package com.vml.tutorial.plantshop.profilePreferences.presentation.paymentMethod.components

sealed interface PaymentMethodEvent {
    data object NavigateBack: PaymentMethodEvent
    data object SaveClicked : PaymentMethodEvent
    data class OnCCNumberChanged(val creditCardNumber: String) : PaymentMethodEvent
    data class OnExpDateChanged(val expirationDate: String) : PaymentMethodEvent
    data class OnCVVChanged(val cvvNumber: String) : PaymentMethodEvent
    data class OnCardHolderNameChanged(val cardHolderName: String) : PaymentMethodEvent
    data object CCInfoVisibilityToggled: PaymentMethodEvent
}
