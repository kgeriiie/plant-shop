package com.vml.tutorial.plantshop.splash.presentation

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.core.data.AppDataStore
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import kotlinx.coroutines.launch

class SplashComponent(
    componentContext: ComponentContext,
    private val appDataStore: AppDataStore,
    private val onNavigateToLogin: () -> Unit,
    private val onNavigateToMain: () -> Unit,
) : ComponentContext by componentContext {

    init {
        componentCoroutineScope().launch {
            if (appDataStore.appLaunchedBefore()) {
                onNavigateToLogin.invoke()
            }
        }
    }

    fun start() {
        componentCoroutineScope().launch {
            appDataStore.setAppLaunchedBefore(true)
        }

        onNavigateToLogin.invoke()
    }
}