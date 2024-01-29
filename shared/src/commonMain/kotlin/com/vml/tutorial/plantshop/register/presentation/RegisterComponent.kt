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
            is RegisterEvent.EmailChanged -> {
                _uiState.update { it.copy(email = event.email) }
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

            RegisterEvent.NavigateBack -> onNavigateBack()
        }
    }

    private fun registerUser() {
        if (isInfoValid()) {
            componentCoroutineScope().launch {
                _uiState.update { it.copy(loading = true, errorMessage = null) }
                when (val result = authRepository.register(
                    uiState.value.email,
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
            uiState.value.email,
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
                email = uiState.value.email,
                birthDate = uiState.value.birthDate,
                phoneNumber = uiState.value.phoneNumber,
                address = uiState.value.address
            )
        )
    }

    private fun isInfoValid(): Boolean {
        return uiState.value.email.isValidEmail() &&
                uiState.value.firstPassword.isNotEmpty() &&
                uiState.value.secondPassword.isNotEmpty() &&
                uiState.value.firstPassword == uiState.value.secondPassword
    }
}
