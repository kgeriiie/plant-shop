package com.vml.tutorial.plantshop.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.vml.tutorial.plantshop.core.utils.exts.orZero

@Composable
fun DefaultDialog(
    title: String,
    message: String,
    primaryText: String,
    primaryCallback: (() -> Unit)? = null,
    secondaryText: String? = null,
    secondaryCallback: (() -> Unit)? = null,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        fontSize = 18.sp
                    )
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text = message,
                        color = Color.Gray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(20.dp))
                    if (primaryText.length > 5 || secondaryText?.length.orZero() > 5) {
                        VerticalArrangedButtons(
                            primaryText = primaryText,
                            primaryCallback = primaryCallback,
                            secondaryText = secondaryText,
                            secondaryCallback = secondaryCallback,
                            onDismissRequest = onDismissRequest
                        )
                    } else {
                        HorizontalArrangedButtons(
                            primaryText = primaryText,
                            primaryCallback = primaryCallback,
                            secondaryText = secondaryText,
                            secondaryCallback = secondaryCallback,
                            onDismissRequest = onDismissRequest
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HorizontalArrangedButtons(
    modifier: Modifier = Modifier,
    primaryText: String,
    primaryCallback: (() -> Unit)? = null,
    secondaryText: String? = null,
    secondaryCallback: (() -> Unit)? = null,
    onDismissRequest: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End
    ) {
        Button(
            onClick = {
                primaryCallback?.invoke()
                onDismissRequest.invoke()
            }
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = primaryText
            )
        }
        secondaryText?.let { btnText ->
            Spacer(Modifier.width(10.dp))
            OutlinedButton(
                onClick = {
                    secondaryCallback?.invoke()
                    onDismissRequest.invoke()
                }
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = btnText
                )
            }
        }
    }
}

@Composable
private fun VerticalArrangedButtons(
    modifier: Modifier = Modifier,
    primaryText: String,
    primaryCallback: (() -> Unit)? = null,
    secondaryText: String? = null,
    secondaryCallback: (() -> Unit)? = null,
    onDismissRequest: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                primaryCallback?.invoke()
                onDismissRequest.invoke()
            }
        ) {
            Text(text = primaryText)
        }
        secondaryText?.let { btnText ->
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    secondaryCallback?.invoke()
                    onDismissRequest.invoke()
                }
            ) {
                Text(text = btnText)
            }
        }
    }
}