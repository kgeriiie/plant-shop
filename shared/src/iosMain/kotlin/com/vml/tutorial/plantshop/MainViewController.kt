package com.vml.tutorial.plantshop

import androidx.compose.ui.window.ComposeUIViewController
import com.vml.tutorial.plantshop.di.AppModule

fun MainViewController() = ComposeUIViewController {
    App(
        appModule = AppModule()
    )
}