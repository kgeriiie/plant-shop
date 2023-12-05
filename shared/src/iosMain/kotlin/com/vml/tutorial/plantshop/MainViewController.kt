package com.vml.tutorial.plantshop

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.vml.tutorial.plantshop.di.AppModule
import com.vml.tutorial.plantshop.navigation.RootComponent

fun MainViewController() = ComposeUIViewController {
    val root = remember {
        RootComponent(
            DefaultComponentContext(LifecycleRegistry()),
            AppModule()
        )
    }

    App(root)
}