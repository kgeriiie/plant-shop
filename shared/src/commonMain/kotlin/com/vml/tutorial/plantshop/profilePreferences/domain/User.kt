package com.vml.tutorial.plantshop.profilePreferences.domain

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val cId: String,
    var firstName: String?,
    var lastName: String?,
    val email: String,
    var birthDate: Long?,
    var phoneNumber: String?,
    var address: Address?,
    var paymentMethod: PaymentMethod?
)

@Serializable
data class Address (
    var streetName: String?,
    var doorNumber: Int?,
    var city: String?,
    var postalCode: Int?,
    var country: String?,
    var additionalDescription: String?
)

@Serializable
data class PaymentMethod(
    val creditCardNumber: String?,
    val expirationDate: String?,
    val cvv: String?,
    val cardHolderName: String?
)
