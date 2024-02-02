package com.vml.tutorial.plantshop.register.presentation.components

sealed interface RegisterEvent {
    data object RegisterClicked : RegisterEvent
    data class EmailChanged(val email: String) : RegisterEvent
    data class FirstPasswordChanged(val firstPassword: String) : RegisterEvent
    data class SecondPasswordChanged(val secondPassword: String) : RegisterEvent
    data class FirstNameChanged(val firstName: String) : RegisterEvent
    data class LastNameChanged(val lastName: String) : RegisterEvent
    data object NavigateBack : RegisterEvent
}
