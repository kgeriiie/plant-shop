package com.vml.tutorial.plantshop.profilePreferences.presentation.preferences.components

sealed interface PreferencesEvent {
    data object OnEditAddressClicked : PreferencesEvent
    data object OnEditPersonalDataClicked : PreferencesEvent
    data object OnPaymentMethodClicked : PreferencesEvent
    data object OnDeleteUserClicked : PreferencesEvent
    data class OnDeleteUserConfirmed(val email: String, val password: String) : PreferencesEvent
    data object OnDeleteUserDialogDismissed : PreferencesEvent
    data object NavigateBack : PreferencesEvent
}
