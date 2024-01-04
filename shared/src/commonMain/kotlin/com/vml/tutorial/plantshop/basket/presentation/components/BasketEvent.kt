package com.vml.tutorial.plantshop.basket.presentation.components

sealed interface BasketEvent {
    data class OnQuantityChanged(val plantId: Int, val value: Int): BasketEvent
    data object Checkout: BasketEvent
    data object ExplorePlants: BasketEvent
}