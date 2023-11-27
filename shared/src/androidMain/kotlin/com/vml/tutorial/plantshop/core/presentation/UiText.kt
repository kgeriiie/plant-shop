package com.vml.tutorial.plantshop.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun UiText.asString(): String {
    return when(this) {
        is UiText.DynamicString -> value
        is UiText.StringRes -> resId.getString(LocalContext.current)
        UiText.Empty -> ""
    }
}