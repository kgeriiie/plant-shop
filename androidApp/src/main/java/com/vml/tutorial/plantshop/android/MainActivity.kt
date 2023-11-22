package com.vml.tutorial.plantshop.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import com.vml.tutorial.plantshop.App
import com.vml.tutorial.plantshop.di.AppModule

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(
                appModule = AppModule(LocalContext.current)
            )
        }
    }
}
