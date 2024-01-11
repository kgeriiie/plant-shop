package com.vml.tutorial.plantshop.core.domain

import com.vml.tutorial.plantshop.core.presentation.UiText

sealed interface DataResult<T> {
    data class Success<T>(val data: T, val message: UiText? = null) : DataResult<T>
    data class Failed<T>(val data: T? = null, val message: UiText? = null) : DataResult<T>
}