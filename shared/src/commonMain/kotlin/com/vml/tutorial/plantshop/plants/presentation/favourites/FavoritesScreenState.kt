package com.vml.tutorial.plantshop.plants.presentation.favourites

import com.vml.tutorial.plantshop.plants.domain.Plant

data class FavoritesScreenState (
    val favoritePlants: List<Plant> = emptyList()
)