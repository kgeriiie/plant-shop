package com.vml.tutorial.plantshop.core.presentation

import androidx.compose.runtime.Composable
import dev.icerock.moko.resources.StringResource

sealed interface UiText {
    data class DynamicString(val value: String) : UiText
    data class StringRes(val resId: StringResource, val args: List<Any> = listOf()) : UiText
    object Empty : UiText
}

@Composable
expect fun UiText.asString(): String