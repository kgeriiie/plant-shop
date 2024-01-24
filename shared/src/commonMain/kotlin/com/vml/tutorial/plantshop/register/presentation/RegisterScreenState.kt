package com.vml.tutorial.plantshop.register.presentation

import com.vml.tutorial.plantshop.core.presentation.UiText

data class RegisterUiState(
    val email: String = "",
    val firstPassword: String = "",
    val secondPassword: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val showDatePickerDialog: Boolean = false,
    val birthDate: String = "DD/MM/YYYY",
    val streetName: String = "",
    val doorNumber: Int? = null,
    val city: String = "",
    val postalCode: Int? = null,
    val country: String = "",
    val additionalDescription: String = "",
    val errorMessage: UiText? = null,
    val loading: Boolean = false
)
