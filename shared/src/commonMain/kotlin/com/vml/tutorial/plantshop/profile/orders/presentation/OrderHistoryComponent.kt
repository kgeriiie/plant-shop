package com.vml.tutorial.plantshop.profile.orders.presentation

import com.arkivanov.decompose.ComponentContext

import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.profile.orders.data.OrdersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderHistoryComponent(
    componentContext: ComponentContext,
    private val ordersRepository: OrdersRepository,
    private val plantsRepository: PlantsRepository,
    private val onComponentEvent: (event: OrderHistoryEvents) -> Unit
): ComponentContext by componentContext  {
    private val _uiState: MutableStateFlow<OrderHistoryUiState> = MutableStateFlow(OrderHistoryUiState())
    val uiState: StateFlow<OrderHistoryUiState> = _uiState.asStateFlow()

    init {
        fetchOrderHistory()
    }

    private fun fetchOrderHistory() {
        componentCoroutineScope().launch {
            _uiState.update { it.copy(contentLoading = true) }

            val orders = ordersRepository.getOrders()
            _uiState.update {
                it.copy(
                    contentLoading = false,
                    items = orders.map { order ->
                        OrderListItemUiState(
                            data = order,
                            plantsMap = buildMap {
                                order.plantIds.forEach { plantId ->
                                    if (!this.containsKey(plantId)) {
                                        plantsRepository.getPlant(plantId)
                                            ?.let { plant -> this.put(plant.id, plant) }
                                    }
                                }
                            }
                        )
                    }
                )
            }
        }
    }

    fun onEvent(event: OrderHistoryEvents) {
        // TODO: handle events.
        when(event) {
            is OrderHistoryEvents.PrimaryButtonPressed -> Unit
            is OrderHistoryEvents.SecondaryButtonPressed -> Unit
            is OrderHistoryEvents.ShowAllPressed -> Unit
            OrderHistoryEvents.StartOrderPressed -> Unit
            else -> onComponentEvent.invoke(event)
        }
    }
}
