package com.vml.tutorial.plantshop.profile.orders.presentation.states

import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem
import com.vml.tutorial.plantshop.profile.orders.domain.OrderStatus

data class OrderListItemUiState(
    val data: OrderItem,
    private val plantsMap: Map<Int, Plant>
) {
    val orderId: UiText
        get() {
        return UiText.StringRes(MR.strings.orders_item_order_id_text, listOf(data.orderNumber))
    }
    val title: UiText
        get() {
        return UiText.DynamicString(plantsMap.values.joinToString { it.name })
    }

    val subTitle: UiText
        get() {
        return data.getMessage()
    }

    val totalPrice: UiText
        get() {
        return UiText.DynamicString("${data.currency}${data.totalPrice}")
    }

    // Include all available plants only once
    val standalonePlants: List<Plant> get() {
        return plantsMap.values.toList()
    }
    
    val allPlants: List<Plant> get() {
        return data.plantIds.mapNotNull { plantsMap[it] }
    }

    val primaryButtonTitle: UiText
        get() {
        return when(data.status) {
            OrderStatus.PENDING -> UiText.StringRes(MR.strings.orders_item_cancel_order_text)
            OrderStatus.SHIPPED -> UiText.StringRes(MR.strings.orders_item_rate_order_text)
            OrderStatus.CANCELLED -> UiText.StringRes(MR.strings.orders_item_rate_us_text)
        }
    }

    val secondaryButtonTitle: UiText
        get() {
        return when(data.status) {
            OrderStatus.PENDING -> UiText.StringRes(MR.strings.orders_item_track_order_text)
            OrderStatus.SHIPPED -> UiText.StringRes(MR.strings.orders_item_re_order_text)
            OrderStatus.CANCELLED -> UiText.StringRes(MR.strings.orders_item_re_order_text)
        }
    }
}
