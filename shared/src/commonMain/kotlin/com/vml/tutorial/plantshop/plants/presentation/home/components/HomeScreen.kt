package com.vml.tutorial.plantshop.plants.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun HomeScreen() {
    // TODO: add state and, onEvent to param list
    Scaffold {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Hello Yusuf, finish me :)",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}