package com.vml.tutorial.plantshop.core.presentation

import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.format

@Composable
actual fun UiText.asString(): String {
    return when(this) {
        is UiText.DynamicString -> value
        is UiText.StringRes -> resId.format(args).toString()
        UiText.Empty -> ""
    }
}