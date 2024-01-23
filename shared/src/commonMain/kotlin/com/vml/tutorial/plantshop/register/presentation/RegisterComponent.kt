package com.vml.tutorial.plantshop.register.presentation

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.data.account.AuthRepository
import com.vml.tutorial.plantshop.core.domain.DataResult
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.core.utils.exts.isValidEmail
import com.vml.tutorial.plantshop.core.utils.formatDate
import com.vml.tutorial.plantshop.profile.domain.Address
import com.vml.tutorial.plantshop.profile.domain.User
import com.vml.tutorial.plantshop.register.data.RegisterUserRepository
import com.vml.tutorial.plantshop.register.presentation.components.RegisterEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

class RegisterComponent(
    componentContext: ComponentContext,
    private val authRepository: AuthRepository,
    private val registerUserRepository: RegisterUserRepository,
    private val onNavigateBack: () -> Unit,
    private val onNavigateToMain: () -> Unit
) :
    ComponentContext by componentContext {
    private val _uiState: MutableStateFlow<RegisterUiState> = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            RegisterEvent.RegisterClicked -> registerUser()
            is RegisterEvent.UsernameChanged -> {
                _uiState.update { it.copy(username = event.username) }
            }

            is RegisterEvent.FirstPasswordChanged -> {
                _uiState.update { it.copy(firstPassword = event.firstPassword) }
            }

            is RegisterEvent.SecondPasswordChanged -> {
                _uiState.update { it.copy(secondPassword = event.secondPassword) }
            }

            is RegisterEvent.FirstNameChanged -> {
                _uiState.update { it.copy(firstName = event.firstName) }
            }

            is RegisterEvent.LastNameChanged -> {
                _uiState.update { it.copy(lastName = event.lastName) }
            }

            is RegisterEvent.PhoneNumberChanged -> {
                _uiState.update { it.copy(phoneNumber = event.phoneNumber) }
            }

            is RegisterEvent.DateConfirmed -> {
                _uiState.update { it.copy(birthDate = formatDate(event.birthDate, DATE_FORMAT)) }
            }

            RegisterEvent.DismissBirthdayDialog -> {
                _uiState.update { it.copy(showDatePickerDialog = false) }
            }

            RegisterEvent.ShowBirthdayDialog -> {
                _uiState.update { it.copy(showDatePickerDialog = true) }
            }

            is RegisterEvent.StreetNameChanged -> {
                _uiState.update { it.copy(streetName = event.streetName) }
            }

            is RegisterEvent.DoorNumberChanged -> {
                _uiState.update { it.copy(doorNumber = event.doorNumber) }
            }

            is RegisterEvent.CityChanged -> {
                _uiState.update { it.copy(city = event.city) }
            }

            is RegisterEvent.CountryChanged -> {
                _uiState.update { it.copy(country = event.country) }
            }

            is RegisterEvent.PostalCodeChanged -> {
                _uiState.update { it.copy(postalCode = event.postalCode) }
            }

            is RegisterEvent.AdditionalDescriptionChanged -> {
                _uiState.update { it.copy(additionalDescription = event.additionalDescription) }
            }

            RegisterEvent.NavigateBack -> onNavigateBack()
        }
    }

    private fun registerUser() {
        if (isInfoValid()) {
            componentCoroutineScope().launch {
                _uiState.update { it.copy(loading = true, errorMessage = null) }
                when (val result = authRepository.register(
                    uiState.value.username,
                    uiState.value.firstPassword
                )) {
                    is DataResult.Failed -> _uiState.update {
                        it.copy(loading = false, errorMessage = result.message)
                    }

                    is DataResult.Success -> {
                        saveUserInfo()
                        signUserIn()
                    }
                }
            }
        } else {
            _uiState.update { it.copy(errorMessage = UiText.StringRes(MR.strings.register_error_text)) }
        }
    }

    private suspend fun signUserIn() {
        when (val result = authRepository.login(
            uiState.value.username,
            uiState.value.firstPassword
        )) {
            is DataResult.Failed -> _uiState.update {
                it.copy(
                    loading = false,
                    errorMessage = result.message
                )
            }

            is DataResult.Success -> _uiState.updateAndGet { it.copy(loading = false) }
                .also { onNavigateToMain.invoke() }
        }
    }

    private suspend fun saveUserInfo() {
        registerUserRepository.insertToDatabase(
            User(
                firstName = uiState.value.firstName,
                lastName = uiState.value.lastName,
                email = uiState.value.username,
                birthDate = uiState.value.birthDate,
                phoneNumber = uiState.value.phoneNumber,
                address = Address(
                    streetName = uiState.value.streetName,
                    doorNumber = uiState.value.doorNumber,
                    city = uiState.value.city,
                    postalCode = uiState.value.postalCode,
                    country = uiState.value.country,
                    additionalDescription = uiState.value.additionalDescription
                )
            )
        )
    }

    private fun isInfoValid(): Boolean {
        return uiState.value.username.isValidEmail() &&
                return uiState.value.firstPassword.isNotEmpty() &&
                        uiState.value.secondPassword.isNotEmpty() &&
                        uiState.value.firstPassword == uiState.value.secondPassword
    }

    companion object {
        const val DATE_FORMAT = "dd/MM/yyyy"
    }
}
