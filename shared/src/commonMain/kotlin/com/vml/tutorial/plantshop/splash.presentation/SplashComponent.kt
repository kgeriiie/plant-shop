package com.vml.tutorial.plantshop.splash.presentation

import com.arkivanov.decompose.ComponentContext

class SplashComponent(
    componentContext: ComponentContext,
    private val onNavigateToMain: () -> Unit,
) : ComponentContext by componentContext {
    fun start() {
        onNavigateToMain.invoke()
    }
}