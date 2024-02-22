package com.vml.tutorial.plantshop.profilePreferences.presentation.profile

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.data.account.AuthRepository
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.profilePreferences.data.ProfileRepository
import com.vml.tutorial.plantshop.profilePreferences.domain.User
import com.vml.tutorial.plantshop.profilePreferences.presentation.profile.components.ProfileEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileComponent(
    componentContext: ComponentContext,
    user: User?,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val onComponentEvent: (event: ProfileEvent) -> Unit,
    private val onShowMessage: (message: UiText) -> Unit,
    private val onLogout: () -> Unit
) : ComponentContext by componentContext {
    private val _state = MutableStateFlow(ProfileState(user))
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        if (user == null) {
            onShowMessage(UiText.StringRes(MR.strings.retrieve_user_error_text))
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.OnGetHelpClicked -> TODO()
            ProfileEvent.OnLogOutClicked -> {
                componentCoroutineScope().launch {
                    authRepository.logout()
                    profileRepository.logout()
                    onLogout()
                }
            }
            ProfileEvent.OnMyOrdersClicked -> TODO()
            ProfileEvent.OnPreferencesClicked -> {
                onComponentEvent.invoke(ProfileEvent.OnPreferencesClicked)
            }

            else -> onComponentEvent.invoke(event)
        }
    }
}
