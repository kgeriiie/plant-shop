package com.vml.tutorial.plantshop.core.presentation

import androidx.compose.runtime.Composable

@Composable
actual fun UiText.asString(): String {
    return when(this) {
        is UiText.DynamicString -> value
        is UiText.StringRes -> resId.toString()
        UiText.Empty -> ""
    }
}