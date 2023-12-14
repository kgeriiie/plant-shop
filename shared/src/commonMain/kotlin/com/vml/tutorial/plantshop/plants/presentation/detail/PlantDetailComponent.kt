package com.vml.tutorial.plantshop.plants.presentation.detail

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.Plant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlantDetailComponent(
    plant: Plant,
    componentContext: ComponentContext,
    private val plantsRepository: PlantsRepository,
    private val onNavigateToBack: () -> Unit
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
            PlantDetailEvent.CheckoutPlant -> TODO()
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

            PlantDetailEvent.OnShareClick -> TODO()
            PlantDetailEvent.NavigateBack -> onNavigateToBack()
        }
    }
}
