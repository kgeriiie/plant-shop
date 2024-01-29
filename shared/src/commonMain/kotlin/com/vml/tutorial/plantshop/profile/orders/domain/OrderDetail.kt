package com.vml.tutorial.plantshop.profile.orders.domain

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.MarkunreadMailbox
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.rounded.Inventory
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.ui.graphics.vector.ImageVector
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.presentation.UiText
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderDetail(
    val actionState: OrderActionState,
    val createdAt: Long
) {
    companion object {
        val completeAfter = mapOf<OrderActionState, Long>(
            Pair(OrderActionState.PLACED, 0),
            Pair(OrderActionState.PROCESSING, 43200),
            Pair(OrderActionState.READY_TO_SHIP, 86400),
            Pair(OrderActionState.SHIPPING, 129600),
            Pair(OrderActionState.COMPLETED, 172800)
        )

        val orderStates = listOf(
            OrderActionState.PLACED,
            OrderActionState.PROCESSING,
            OrderActionState.READY_TO_SHIP,
            OrderActionState.SHIPPING,
            OrderActionState.COMPLETED,
        )
    }
}

@Serializable
enum class OrderActionState {
    @SerialName("placed") PLACED,
    @SerialName("processing") PROCESSING,
    @SerialName("ready_to_ship") READY_TO_SHIP,
    @SerialName("shipping") SHIPPING,
    @SerialName("completed") COMPLETED
}

val OrderActionState.title: UiText
    get() {
    return when(this) {
        OrderActionState.PLACED -> MR.strings.track_placed_title
        OrderActionState.PROCESSING -> MR.strings.track_processing_title
        OrderActionState.READY_TO_SHIP -> MR.strings.track_ready_title
        OrderActionState.SHIPPING -> MR.strings.track_shipping_title
        OrderActionState.COMPLETED -> MR.strings.track_completed_title
    }.let(UiText::StringRes)
}

val OrderActionState.subtitle: UiText
    get() {
    return when(this) {
        OrderActionState.PLACED -> MR.strings.track_placed_subtitle
        OrderActionState.PROCESSING -> MR.strings.track_processing_subtitle
        OrderActionState.READY_TO_SHIP -> MR.strings.track_ready_subtitle
        OrderActionState.SHIPPING -> MR.strings.track_shipping_subtitle
        OrderActionState.COMPLETED -> MR.strings.track_completed_subtitle
    }.let(UiText::StringRes)
}

val OrderActionState.icon: ImageVector
    get() {
    return when(this) {
        OrderActionState.PLACED -> Icons.Rounded.ReceiptLong
        OrderActionState.PROCESSING -> Icons.Outlined.ShoppingBag
        OrderActionState.READY_TO_SHIP -> Icons.Rounded.Inventory
        OrderActionState.SHIPPING -> Icons.Outlined.LocalShipping
        OrderActionState.COMPLETED -> Icons.Outlined.MarkunreadMailbox
    }
}