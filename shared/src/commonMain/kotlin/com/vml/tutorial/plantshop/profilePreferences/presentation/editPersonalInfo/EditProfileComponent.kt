package com.vml.tutorial.plantshop.profilePreferences.presentation.editPersonalInfo

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.core.utils.formatDate
import com.vml.tutorial.plantshop.profilePreferences.data.ProfileRepository
import com.vml.tutorial.plantshop.profilePreferences.domain.User
import com.vml.tutorial.plantshop.profilePreferences.presentation.editPersonalInfo.components.EditProfileEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileComponent(
    componentContext: ComponentContext,
    private val user: User?,
    private val profileRepository: ProfileRepository,
    private val onShowMessage: (message: UiText) -> Unit,
    private val onComponentEvent: (event: EditProfileEvent) -> Unit
) : ComponentContext by componentContext {
    private val _state = MutableStateFlow(EditProfileScreenState(user = user))
    val state: StateFlow<EditProfileScreenState> = _state.asStateFlow()

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.FirstNameChanged -> {
                _state.update { it.copy(firstName = event.firstName) }
            }

            is EditProfileEvent.LastNameChanged -> {
                _state.update { it.copy(lastName = event.lastName) }
            }

            is EditProfileEvent.PhoneNumberChanged -> {
                _state.update { it.copy(phoneNumber = event.phoneNumber) }
            }

            is EditProfileEvent.DateConfirmed -> {
                _state.update { it.copy(birthDate = event.birthDate) }
            }

            EditProfileEvent.DismissBirthdayDialog -> {
                _state.update { it.copy(showDatePickerDialog = false) }
            }

            EditProfileEvent.ShowBirthdayDialog -> {
                _state.update { it.copy(showDatePickerDialog = true) }
            }

            EditProfileEvent.SaveClicked -> {
                user.apply {
                    this?.firstName = state.value.firstName
                    this?.lastName = state.value.lastName
                    this?.phoneNumber = state.value.phoneNumber
                    this?.birthDate = state.value.birthDate
                }?.let { updateUser(it) }
            }

            else -> onComponentEvent(event)
        }
    }

    private fun updateUser(userInfo: User) {
        componentCoroutineScope().launch {
            _state.update { it.copy(loading = true) }
            if (!profileRepository.updateUserInfo(userInfo)) {
                _state.update { it.copy(errorMessage = UiText.StringRes(MR.strings.save_error_text)) }
            } else {
                onShowMessage(UiText.StringRes(MR.strings.saved_text))
            }
            _state.update { it.copy(loading = false) }
        }
    }
}
