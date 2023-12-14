package com.vml.tutorial.plantshop.plants.presentation.home

import com.vml.tutorial.plantshop.plants.domain.Plant

data class HomeScreenState(
    val plants: List<Plant> = emptyList()
)
