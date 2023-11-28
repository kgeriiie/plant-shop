package com.vml.tutorial.plantshop.plants.presentation.detail

sealed interface PlantDetailEvent {
    object OnShareClick: PlantDetailEvent
    object OnFavouriteClick: PlantDetailEvent
    data class OnQuantityChanged(val value: Int): PlantDetailEvent
    object CheckoutPlant: PlantDetailEvent
    object NavigateBack: PlantDetailEvent
}