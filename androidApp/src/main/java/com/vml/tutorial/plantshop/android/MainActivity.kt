package com.vml.tutorial.plantshop.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.retainedComponent
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.vml.tutorial.plantshop.App
import com.vml.tutorial.plantshop.di.AppModule
import com.vml.tutorial.plantshop.DefaultAppComponent

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalDecomposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = retainedComponent { context ->
            DefaultAppComponent(
                componentContext = context,
                appModule = AppModule(this)
            )
        }
        Firebase.initialize(this)

        setContent {
            App(root)
        }
    }
}
