package com.vml.tutorial.plantshop.register.presentation

import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.profile.domain.Address

data class RegisterUiState(
    val email: String = "",
    val firstPassword: String = "",
    val secondPassword: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val showDatePickerDialog: Boolean = false,
    val birthDate: String? = null,
    val address: Address? = null,
    val errorMessage: UiText? = null,
    val loading: Boolean = false
)
