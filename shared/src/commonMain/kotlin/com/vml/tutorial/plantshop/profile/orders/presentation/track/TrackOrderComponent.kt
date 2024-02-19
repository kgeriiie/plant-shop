package com.vml.tutorial.plantshop.profile.orders.presentation.track

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.core.utils.exts.orZero
import com.vml.tutorial.plantshop.profile.data.ProfileRepository
import com.vml.tutorial.plantshop.profile.domain.Address
import com.vml.tutorial.plantshop.profile.orders.data.OrdersRepository
import com.vml.tutorial.plantshop.profile.orders.domain.OrderActionState
import com.vml.tutorial.plantshop.profile.orders.domain.OrderDetail
import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem
import com.vml.tutorial.plantshop.profile.orders.domain.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class TrackOrderComponent(
    componentContext: ComponentContext,
    order: OrderItem,
    private val ordersRepository: OrdersRepository,
    private val profileRepository: ProfileRepository,
    private val onComponentEvent: (event: TrackOrderEvents.ComponentEvents) -> Unit
): ComponentContext by componentContext  {

    private val addressFlow: MutableStateFlow<Address?> = MutableStateFlow(null)
    private val _uiState: MutableStateFlow<TrackOrderUiState> = MutableStateFlow(TrackOrderUiState(order))
    val uiState: StateFlow<TrackOrderUiState> = combine(_uiState, addressFlow) { state, address ->
        state.copy(address = address)
    }.stateIn(componentCoroutineScope(), SharingStarted.WhileSubscribed(), _uiState.value)

    init {
        fetchAddress()
        updateOrderDetails()
    }

    fun onEvent(event: TrackOrderEvents) {
        when (event) {
            is TrackOrderEvents.StateDidPressed -> {
                uiState.value.details.firstOrNull { it.detail.actionState == event.state }?.let { state ->
                    if (state.clickable) {
                        _uiState.update { it.copy(confirmStateCompletion = event.state) }
                    }
                }
            }
            is TrackOrderEvents.ConfirmDialogDismissed -> {
                if (event.confirmed) {
                    markAsCompleted(listOf(OrderDetail(
                        actionState = event.item,
                        createdAt = Clock.System.now().epochSeconds
                    )))
                }
                _uiState.update { it.copy(confirmStateCompletion = null) }
            }
            is TrackOrderEvents.ComponentEvents -> onComponentEvent(event)
        }
    }

    private fun fetchAddress() {
        componentCoroutineScope().launch {
            addressFlow.value = profileRepository.getUser()?.address
        }
    }

    private fun markAsCompleted(details: List<OrderDetail>) {
        val completed = details.any { it.actionState == OrderActionState.COMPLETED }
        componentCoroutineScope().launch {
            val order = _uiState.value.order
            _uiState.update { state ->
                state.copy(
                    order = ordersRepository.updateOrder(
                        state.order.copy(
                            status = OrderStatus.SHIPPED.takeIf { completed }?: state.order.status,
                            details = buildList {
                            addAll(state.order.details)
                            addAll(details)
                        }
                    ))?: order
                )
            }
        }
    }

    private fun updateOrderDetails() {
        // We don't have an active delivery service :D So the order details will be updated by app.
        val order = _uiState.value.order
        val needToUpdate = mutableListOf<OrderDetail>()
        OrderDetail.orderStates.forEach { state ->
            val stateCompletionTime = order.createdAt + OrderDetail.completeAfter[state].orZero()
            if (order.details.none { it.actionState == state } && Clock.System.now().epochSeconds >= stateCompletionTime) {
                needToUpdate.add(OrderDetail(
                    actionState = state,
                    createdAt = stateCompletionTime
                ))
            }
        }

        if (needToUpdate.isNotEmpty()) {
            markAsCompleted(needToUpdate)
        }
    }
}