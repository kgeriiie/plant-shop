package com.vml.tutorial.plantshop.core.presentation

import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.StringResource

sealed interface UiText {
    data class DynamicString(val value: String) : UiText
    class StringRes(val resId: StringResource) : UiText
    object Empty : UiText
}

@Composable
expect fun UiText.asString(): String