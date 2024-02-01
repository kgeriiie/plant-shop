package com.vml.tutorial.plantshop.register.presentation

import com.vml.tutorial.plantshop.core.presentation.UiText

data class RegisterUiState(
    val email: String = "",
    val firstPassword: String = "",
    val secondPassword: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val errorMessage: UiText? = null,
    val loading: Boolean = false
)
