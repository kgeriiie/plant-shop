package com.vml.tutorial.plantshop.profilePreferences.domain

import kotlinx.serialization.Serializable

@Serializable
data class User (
    var cId: String? = null,
    var firstName: String?,
    var lastName: String?,
    val email: String,
    var birthDate: Long? = null,
    var phoneNumber: String? = null,
    var address: Address? = null,
    var paymentMethod: PaymentMethod? = null
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
