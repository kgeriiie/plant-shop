package com.vml.tutorial.plantshop.profilePreferences.presentation.profile

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.profilePreferences.domain.User
import com.vml.tutorial.plantshop.profilePreferences.presentation.profile.components.ProfileEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileComponent(
    componentContext: ComponentContext,
    user: User?,
    private val onComponentEvent: (event: ProfileEvent) -> Unit
) : ComponentContext by componentContext {
    private val _state = MutableStateFlow(ProfileState(user))
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.OnGetHelpClicked -> TODO()
            ProfileEvent.OnLogOutClicked -> TODO()
            ProfileEvent.OnMyOrdersClicked -> TODO()
            ProfileEvent.OnPreferencesClicked -> {
                onComponentEvent.invoke(ProfileEvent.OnPreferencesClicked)
            }
            else -> onComponentEvent.invoke(event)
        }
    }
}
