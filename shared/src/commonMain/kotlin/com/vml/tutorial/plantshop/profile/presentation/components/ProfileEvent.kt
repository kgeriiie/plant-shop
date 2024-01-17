package com.vml.tutorial.plantshop.profile.presentation.components

sealed interface ProfileEvent {
    data object NavigateBack: ProfileEvent
    data object OnMyOrdersClick: ProfileEvent
    data object OnPaymentMethodClick: ProfileEvent
    data object OnGetHelpClick: ProfileEvent
    data object OnSettingsClick: ProfileEvent
    data object OnLogOutClick: ProfileEvent
}