package com.vml.tutorial.plantshop.plants.presentation.detail

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.basket.data.BasketRepository
import com.vml.tutorial.plantshop.core.utils.ShareUtils
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.detail.components.PlantDetailEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlantDetailComponent(
    plant: Plant,
    componentContext: ComponentContext,
    private val shareUtils: ShareUtils,
    private val plantsRepository: PlantsRepository,
    private val basketRepository: BasketRepository,
    private val onComponentEvent: (event: PlantDetailEvent) -> Unit,
) : ComponentContext by componentContext {
    private val _state = MutableStateFlow(PlantDetailState(plant))
    val state: StateFlow<PlantDetailState> =
        _state.combine(plantsRepository.getFavorites()) { state, favourites ->
            state.copy(
                isFavourite = favourites.any { it.id == state.plant.id }
            )
        }.stateIn(componentCoroutineScope(), SharingStarted.WhileSubscribed(), _state.value)

    fun onEvent(event: PlantDetailEvent) {
        when (event) {
            PlantDetailEvent.CheckoutPlant -> {
                componentCoroutineScope().launch {
                    basketRepository.insertItem(state.value.plant.id, state.value.quantity)
                    _state.update { it.copy(
                        showAddToBasketDialog = true,
                        quantity = 1
                    )}
                }
            }
            PlantDetailEvent.OnFavouriteClick -> {
                componentCoroutineScope().launch {
                    plantsRepository.toggleFavoriteStatus(state.value.plant.id)
                }
            }

            is PlantDetailEvent.OnQuantityChanged -> {
                if (event.value >= 1) {
                    _state.update { it.copy(quantity = event.value) }
                }
            }

            is PlantDetailEvent.OnShareClick -> shareUtils.shareContent(event.content)
            PlantDetailEvent.DismissDialog -> _state.update { it.copy(showAddToBasketDialog = false) }
            else -> onComponentEvent.invoke(event)
        }
    }
}
