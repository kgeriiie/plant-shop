package com.vml.tutorial.plantshop.login.presentation

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.MR.strings.login_error_text
import com.vml.tutorial.plantshop.core.data.account.AuthRepository
import com.vml.tutorial.plantshop.core.domain.DataResult
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.core.utils.exts.isValidEmail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

class LoginComponent(
    componentContext: ComponentContext,
    private val authRepository: AuthRepository,
    private val onNavigateToMain: () -> Unit,
): ComponentContext by componentContext {

    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.PasswordChanged -> _uiState.update { it.copy(password = event.value, errorMessage = null) }
            is LoginEvent.UsernameChanged -> _uiState.update { it.copy(username = event.value, errorMessage = null) }
            LoginEvent.PasswordVisibilityToggled -> _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
            LoginEvent.LoginClicked -> doLogin()
        }
    }

    private fun doLogin() {
        if (isValid()) {
            componentCoroutineScope().launch {
                _uiState.update { it.copy(loading = true, errorMessage = null) }
                when(val result = authRepository.login(
                    email = _uiState.value.username,
                    password = _uiState.value.password)
                ) {
                    is DataResult.Failed -> _uiState.update { it.copy(loading = false, errorMessage = result.message) }
                    is DataResult.Success -> _uiState.updateAndGet { it.copy(loading = false) }.also { onNavigateToMain.invoke() }
                }
            }
        } else {
            _uiState.update { it.copy(errorMessage = UiText.StringRes(login_error_text)) }
        }
    }

    private fun isValid(): Boolean {
        return _uiState.value.username.isValidEmail() && _uiState.value.password.isNotEmpty()
    }
}

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val errorMessage: UiText? = null,
    val passwordVisible: Boolean = false,
    val loading: Boolean = false
)

sealed interface LoginEvent {
    data class UsernameChanged(val value: String): LoginEvent
    data class PasswordChanged(val value: String): LoginEvent
    data object PasswordVisibilityToggled: LoginEvent
    data object LoginClicked: LoginEvent
}