package com.vml.tutorial.plantshop.profile.domain

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val firstName: String?,
    val lastName: String?,
    val email: String,
    val birthDate: String?,
    val phoneNumber: String?,
    val address: Address?
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