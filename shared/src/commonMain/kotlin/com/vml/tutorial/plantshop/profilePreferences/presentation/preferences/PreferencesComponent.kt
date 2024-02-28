package com.vml.tutorial.plantshop.profilePreferences.presentation.preferences

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.data.account.AuthRepository
import com.vml.tutorial.plantshop.core.domain.DataResult
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.profilePreferences.data.ProfileRepository
import com.vml.tutorial.plantshop.profilePreferences.domain.User
import com.vml.tutorial.plantshop.profilePreferences.presentation.preferences.components.PreferencesEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PreferencesComponent(
    componentContext: ComponentContext,
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository,
    private val onLogout: () -> Unit,
    private val onShowMessage: (message: UiText) -> Unit,
    private val onComponentEvent: (event: PreferencesEvent, user: User?) -> Unit
) : ComponentContext by componentContext {
    private val _state = MutableStateFlow(PreferencesState())
    val state: StateFlow<PreferencesState> = _state.asStateFlow()

    fun onEvent(event: PreferencesEvent) {
        when (event) {
            is PreferencesEvent.OnDeleteUserConfirmed -> {
                componentCoroutineScope().launch {
                    _state.update {
                        it.copy(showConfirmationDialog = false, showProgressDialog = true)
                    }
                    when (authRepository.login(event.email, event.password)) {
                        is DataResult.Failed -> {
                            onShowMessage(UiText.StringRes(MR.strings.delete_error_text))
                        }

                        is DataResult.Success -> {
                            authRepository.deleteUser()
                            profileRepository.getUser()?.cId?.let { profileRepository.deleteUser(it) }
                            onLogout()
                        }
                    }
                    _state.update { it.copy(showProgressDialog = false) }
                }
            }

            PreferencesEvent.OnDeleteUserDialogDismissed -> {
                _state.update { it.copy(showConfirmationDialog = false) }
            }

            PreferencesEvent.OnDeleteUserClicked -> {
                _state.update { it.copy(showConfirmationDialog = true) }
            }
            else -> {
                componentCoroutineScope().launch {
                    onComponentEvent(event, profileRepository.getUser())
                }
            }
        }
    }
}
