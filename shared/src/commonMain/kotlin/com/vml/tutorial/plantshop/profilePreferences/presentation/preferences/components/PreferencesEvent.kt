package com.vml.tutorial.plantshop.profilePreferences.presentation.preferences.components

sealed interface PreferencesEvent {
    data object OnEditAddressClicked : PreferencesEvent
    data object OnEditPersonalDataClicked : PreferencesEvent
    data object OnPaymentMethodClicked : PreferencesEvent
    data object OnDeleteProfileClicked : PreferencesEvent
    data object NavigateBack : PreferencesEvent
}