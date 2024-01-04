package com.vml.tutorial.plantshop.plants.presentation.detail

import com.vml.tutorial.plantshop.plants.domain.Plant

data class PlantDetailState(
    val plant: Plant,
    val isFavourite: Boolean = false,
    val quantity: Int = 1,
    val showAddToBasketDialog: Boolean = false
)