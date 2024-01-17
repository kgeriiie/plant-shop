package com.vml.tutorial.plantshop.profile.presentation

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.profile.ProfileState
import com.vml.tutorial.plantshop.profile.domain.User
import com.vml.tutorial.plantshop.profile.presentation.components.ProfileEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ProfileComponent(
    componentContext: ComponentContext,
    user: User?,
    private val onComponentEvent: (event: ProfileEvent) -> Unit
) : ComponentContext by componentContext {
    private val _state = MutableStateFlow(ProfileState(user))
    val state: StateFlow<ProfileState> = _state

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.OnGetHelpClick -> TODO()
            ProfileEvent.OnLogOutClick -> TODO()
            ProfileEvent.OnMyOrdersClick -> TODO()
            ProfileEvent.OnPaymentMethodClick -> TODO()
            ProfileEvent.OnSettingsClick -> TODO()
            else -> onComponentEvent.invoke(event)
        }
    }
}