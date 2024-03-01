package com.vml.tutorial.plantshop.profilePreferences.presentation.paymentMethod

import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.profilePreferences.domain.PaymentMethod

data class PaymentMethodScreenState (
    val paymentMethod: PaymentMethod? = null,
    val errorMessage: UiText? = null,
    val loading: Boolean = false,
    val creditCardNumber: String? = paymentMethod?.creditCardNumber,
    val expirationDate: String? = paymentMethod?.expirationDate,
    val cardHolderName: String? = paymentMethod?.cardHolderName,
    val cvv: String? = paymentMethod?.cvv,
    val ccInfoVisible: Boolean = false
)
