package com.vml.tutorial.plantshop.plants.presentation.detail

sealed interface PlantDetailEvent {
    data object OnShareClick: PlantDetailEvent
    data object OnFavouriteClick: PlantDetailEvent
    data class OnQuantityChanged(val value: Int): PlantDetailEvent
    data object CheckoutPlant: PlantDetailEvent
    data object NavigateBack: PlantDetailEvent
    data object NavigateToBasket: PlantDetailEvent
    data object DismissDialog: PlantDetailEvent
}