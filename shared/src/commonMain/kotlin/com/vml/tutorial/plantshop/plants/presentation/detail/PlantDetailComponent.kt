package com.vml.tutorial.plantshop.plants.presentation.detail

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.Plant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlantDetailComponent(
    plant: Plant,
    componentContext: ComponentContext,
    private val plantsRepository: PlantsRepository,
    private val onNavigateToBack: () -> Unit
) : ComponentContext by componentContext {
    private val _state = MutableStateFlow(PlantDetailState(plant))
    val state: StateFlow<PlantDetailState> = _state.asStateFlow()

    init {
        _state.update { it.copy(isFavourite = it.plant.isFavorite) }
    }

    fun onEvent(event: PlantDetailEvent) {
        when (event) {
            PlantDetailEvent.CheckoutPlant -> TODO()
            PlantDetailEvent.OnFavouriteClick -> {
                _state.update { it.copy(isFavourite = !it.plant.isFavorite) }
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
