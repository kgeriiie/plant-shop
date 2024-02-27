package com.vml.tutorial.plantshop.profile.orders.presentation.track

import com.vml.tutorial.plantshop.core.presentation.DateTimeFormatter
import com.vml.tutorial.plantshop.core.utils.exts.orZero
import com.vml.tutorial.plantshop.profile.domain.Address
import com.vml.tutorial.plantshop.profile.orders.domain.OrderActionState
import com.vml.tutorial.plantshop.profile.orders.domain.OrderDetail
import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem

data class TrackOrderUiState(
    val order: OrderItem,
    val address: Address? = null,
    val confirmStateCompletion: OrderActionState? = null
) {
    val orderedDate: String get() {
        val dateFormat = "EEE, dd MMM"
        return DateTimeFormatter.format(order.createdAt, dateFormat)
    }

    val estimatedDeliveryDate: String get() {
        val dateFormat = "dd MMMM yyyy"
        return DateTimeFormatter.format(order.createdAt.plus(OrderDetail.completeAfter[OrderActionState.COMPLETED].orZero()), dateFormat)
    }

    private var _details: List<TrackOrderDetailUiState> = listOf()
    val details: List<TrackOrderDetailUiState> get() {
        if (_details.isEmpty() || _details.count { it.detail.createdAt > 0 } != order.details.size) {
            val completed = order.details.sortedBy { it.createdAt }
            var previousWasCompleted: Boolean = completed.isEmpty()
            _details = buildList {
                OrderDetail.orderStates.forEach { state ->
                    val detail = completed.firstOrNull { it.actionState == state }?: OrderDetail(state, 0)
                    val accomplished = detail.createdAt > 0
                    add(TrackOrderDetailUiState(
                        detail = detail,
                        completed = accomplished,
                        clickable = !accomplished && previousWasCompleted,
                        lastCompleted = completed.lastOrNull()?.actionState == state
                    ))
                    previousWasCompleted = accomplished
                }
            }
        }

        return _details
    }

    val deliveryAddress: String? get() {
        return address?.takeIf { it.isFilled() }?.let {
            "${it.streetName} ${it.doorNumber}, ${it.city} ${it.postalCode}, ${it.country}"
        }
    }
}