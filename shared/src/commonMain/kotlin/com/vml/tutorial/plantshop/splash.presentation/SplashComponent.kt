package com.vml.tutorial.plantshop.splash.presentation

import com.arkivanov.decompose.ComponentContext

class SplashComponent(
    componentContext: ComponentContext,
    private val onNavigateToMain: () -> Unit,
) : ComponentContext by componentContext {
    fun start() {
        // TODO: add some extra logic e.g: don't show this screen next time, if the user already clicked on 'Get Started'.
        onNavigateToMain.invoke()
    }
}