package com.vml.tutorial.plantshop.profile.orders.presentation.track

import com.vml.tutorial.plantshop.core.presentation.DateTimeFormatter
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.profile.orders.domain.OrderDetail

data class TrackOrderDetailUiState(
    val detail: OrderDetail,
    val clickable: Boolean = false,
    val completed: Boolean,
    val lastCompleted: Boolean,
) {
    val date: UiText get() {
        val dateFormat = "MMM dd, HH:mm"
        return UiText.DynamicString(DateTimeFormatter.format(detail.createdAt, dateFormat))
    }
}