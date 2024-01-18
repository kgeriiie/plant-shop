package com.vml.tutorial.plantshop.splash.presentation

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.core.data.AppDataStore
import com.vml.tutorial.plantshop.core.data.account.AuthRepository
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.login.presentation.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashComponent(
    componentContext: ComponentContext,
    private val appDataStore: AppDataStore,
    private val authRepository: AuthRepository,
    private val onNavigateToLogin: () -> Unit,
    private val onNavigateToMain: () -> Unit,
) : ComponentContext by componentContext {

    private val _uiState: MutableStateFlow<SplashUiState> = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        componentCoroutineScope().launch {
            if (appDataStore.appLaunchedBefore()) {
                _uiState.update { it.copy(buttonEnabled = false, loading = true) }
                if (authRepository.isAuthenticated()) {
                    onNavigateToMain.invoke()
                } else {
                    onNavigateToLogin.invoke()
                }
            } else {
                _uiState.update { it.copy(buttonEnabled = true, loading = false) }
            }
        }
    }

    fun start() {
        componentCoroutineScope().launch {
            appDataStore.setAppLaunchedBefore(true).also { onNavigateToLogin.invoke() }
        }
    }
}

data class SplashUiState(
    val buttonEnabled: Boolean = false,
    val loading: Boolean = false
)