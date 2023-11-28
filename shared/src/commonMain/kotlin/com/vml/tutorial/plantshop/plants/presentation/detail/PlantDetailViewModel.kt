package com.vml.tutorial.plantshop.plants.presentation.detail

import com.vml.tutorial.plantshop.plants.domain.Plant
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlantDetailViewModel(
    plant: Plant
): ViewModel() {
    private val _state = MutableStateFlow(PlantDetailState(plant))
    val state: StateFlow<PlantDetailState> = _state.asStateFlow()

    fun onEvent(event: PlantDetailEvent) {
        when(event) {
            PlantDetailEvent.CheckoutPlant -> TODO()
            PlantDetailEvent.OnFavouriteClick -> {
                _state.update { it.copy(isFavourite = !it.isFavourite)}
            }
            is PlantDetailEvent.OnQuantityChanged -> {
                if (event.value > 1) {
                    _state.update { it.copy(quantity = event.value) }
                }
            }
            PlantDetailEvent.OnShareClick -> TODO()
        }
    }
}
