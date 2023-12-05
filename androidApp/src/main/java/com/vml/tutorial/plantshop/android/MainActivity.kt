package com.vml.tutorial.plantshop.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.retainedComponent
import com.vml.tutorial.plantshop.App
import com.vml.tutorial.plantshop.di.AppModule
import com.vml.tutorial.plantshop.navigation.RootComponent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = retainedComponent { context ->
            RootComponent(
                componentContext = context,
                appModule = AppModule(this)
            )
        }

        setContent {
            App(root)
        }
    }
}
