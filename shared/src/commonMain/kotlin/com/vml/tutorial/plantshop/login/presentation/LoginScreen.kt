package com.vml.tutorial.plantshop.login.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoginScreen(
    state: LoginUiState,
    onEvent: (event: LoginEvent) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            TextField(
                value = state.username,
                onValueChange = {
                    onEvent.invoke(LoginEvent.UsernameChanged(it))
                }
            )
        }
    }
}