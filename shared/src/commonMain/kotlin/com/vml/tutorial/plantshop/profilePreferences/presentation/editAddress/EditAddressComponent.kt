package com.vml.tutorial.plantshop.profilePreferences.presentation.editAddress

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.profilePreferences.data.ProfileRepository
import com.vml.tutorial.plantshop.profilePreferences.domain.Address
import com.vml.tutorial.plantshop.profilePreferences.domain.User
import com.vml.tutorial.plantshop.profilePreferences.presentation.editAddress.components.EditAddressEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditAddressComponent(
    componentContext: ComponentContext,
    private val user: User?,
    private val profileRepository: ProfileRepository,
    private val onShowMessage: (message: UiText) -> Unit,
    private val onComponentEvent: (event: EditAddressEvent) -> Unit
) : ComponentContext by componentContext {
    private val _state = MutableStateFlow(EditAddressState(address = user?.address))
    val state: StateFlow<EditAddressState> = _state.asStateFlow()

    fun onEvent(event: EditAddressEvent) {
        when (event) {
            is EditAddressEvent.StreetNameChanged -> {
                this._state.update { it.copy(streetName = event.streetName) }
            }

            is EditAddressEvent.DoorNumberChanged -> {
                this._state.update { it.copy(doorNumber = event.doorNumber) }
            }

            is EditAddressEvent.CityChanged -> {
                this._state.update { it.copy(city = event.city) }
            }

            is EditAddressEvent.CountryChanged -> {
                this._state.update { it.copy(country = event.country) }
            }

            is EditAddressEvent.PostalCodeChanged -> {
                this._state.update { it.copy(postalCode = event.postalCode) }
            }

            is EditAddressEvent.AdditionalDescriptionChanged -> {
                this._state.update { it.copy(additionalDescription = event.additionalDescription) }
            }

            is EditAddressEvent.SaveClicked -> {
                user.apply { this?.address = getAddress(state.value) }?.let { updateAddress(it) }
                    ?: run { _state.update { it.copy(errorMessage = UiText.StringRes(MR.strings.save_error_text)) } }
            }

            else -> onComponentEvent(event)
        }
    }

    private fun getAddress(state: EditAddressState): Address {
        return Address(
            streetName = state.streetName,
            doorNumber = state.doorNumber,
            city = state.city,
            country = state.country,
            postalCode = state.postalCode,
            additionalDescription = state.additionalDescription
        )
    }

    private fun updateAddress(userInfo: User) {
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
