package com.vml.tutorial.plantshop

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.vml.tutorial.plantshop.di.AppModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob

fun MainViewController() = ComposeUIViewController {
    val coroutineScope: CoroutineScope = remember {
        CoroutineScope(Dispatchers.IO + SupervisorJob())
    }

    val root = remember {
        DefaultAppComponent(
            DefaultComponentContext(LifecycleRegistry()),
            AppModule(coroutineScope)
        )
    }

    App(root)
}