package com.vml.tutorial.plantshop.login.presentation

import com.arkivanov.decompose.ComponentContext

class LoginComponent(
    componentContext: ComponentContext,
    private val onNavigateToMain: () -> Unit,
): ComponentContext by componentContext {

    fun onEvent(event: LoginEvent) {

    }
}

data class LoginUiState(
    val username: String,
    val password: String
)

sealed interface LoginEvent {
    data class UsernameChanged(val value: String): LoginEvent
    data class PasswordChanged(val value: String): LoginEvent
}