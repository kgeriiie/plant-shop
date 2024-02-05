package com.vml.tutorial.plantshop.profile.orders.domain

import com.vml.tutorial.plantshop.MR.strings.orders_item_cancelled_date_text
import com.vml.tutorial.plantshop.MR.strings.orders_item_pending_date_text
import com.vml.tutorial.plantshop.MR.strings.orders_item_shipped_date_text
import com.vml.tutorial.plantshop.core.presentation.DateTimeFormatter
import com.vml.tutorial.plantshop.core.presentation.UiText
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(
    @SerialName("orderCId") val id: String,
    @SerialName("orderNumber") val orderNumber: String,
    val plantIds: List<Int>,
    val status: OrderStatus,
    val currency: String,
    val totalPrice: Double,
    val createdAt: Long,
    val updatedAt: Long,
    val userId: String,
) {
    fun getMessage(): UiText {
        val dateFormat = "MMM dd, hh:mm"
        return when(status) {
            OrderStatus.PENDING -> UiText.StringRes(orders_item_pending_date_text, listOf(DateTimeFormatter.format(createdAt, dateFormat)))
            OrderStatus.SHIPPED -> UiText.StringRes(orders_item_shipped_date_text, listOf(DateTimeFormatter.format(updatedAt, dateFormat)))
            OrderStatus.CANCELLED -> UiText.StringRes(orders_item_cancelled_date_text, listOf(DateTimeFormatter.format(updatedAt, dateFormat)))
        }
    }
}

@Serializable
enum class OrderStatus {
    @SerialName("pending") PENDING,
    @SerialName("shipped") SHIPPED,
    @SerialName("cancelled") CANCELLED
}