package com.vml.tutorial.plantshop

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.vml.tutorial.plantshop.di.AppModule

fun MainViewController() = ComposeUIViewController {
    val root = remember {
        DefaultAppComponent(
            DefaultComponentContext(LifecycleRegistry()),
            AppModule()
        )
    }

    App(root)
}