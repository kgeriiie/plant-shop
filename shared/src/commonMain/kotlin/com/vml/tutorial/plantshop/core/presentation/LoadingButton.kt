package com.vml.tutorial.plantshop.core.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingButton(buttonText: String, isLoading: Boolean, onClicked: () -> Unit) {
    Button(modifier = Modifier.fillMaxWidth().height(50.dp), onClick = {
        onClicked()
    }) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(buttonText)
        }
    }
}
