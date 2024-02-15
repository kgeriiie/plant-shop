package com.vml.tutorial.plantshop.profilePreferences.presentation.profile.components

sealed interface ProfileEvent {
    data object NavigateBack: ProfileEvent
    data object OnMyOrdersClicked: ProfileEvent
    data object OnGetHelpClicked: ProfileEvent
    data object OnPreferencesClicked: ProfileEvent
    data object OnLogOutClicked: ProfileEvent
}