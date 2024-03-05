package com.vml.tutorial.plantshop.profile.domain

import kotlinx.serialization.Serializable

@Serializable
data class User (
    var cId: String? = null,
    val firstName: String?,
    val lastName: String?,
    val email: String,
    val birthDate: Long? = null,
    val phoneNumber: String? = null,
    val address: Address? = null,
    var paymentMethod: PaymentMethod?
) {
    val monogram: String get() = if (firstName.isNullOrEmpty()) {
        "${email.first()}"
    } else {
        "${firstName.first()} ${lastName?.first()}"
    }.uppercase()
}

@Serializable
data class Address (
    val streetName: String?,
    val doorNumber: Int?,
    val city: String?,
    val postalCode: Int?,
    val country: String?,
    val additionalDescription: String?
)

@Serializable
data class PaymentMethod(
    val creditCardNumber: String?,
    val expirationDate: String?,
    val cvv: String?,
    val cardHolderName: String?
)
