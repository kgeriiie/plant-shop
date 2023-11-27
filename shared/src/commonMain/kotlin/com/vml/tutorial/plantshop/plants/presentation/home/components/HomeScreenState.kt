package com.vml.tutorial.plantshop.plants.presentation.home.components

import com.vml.tutorial.plantshop.plants.domain.Plant

data class HomeScreenState(
    val plants: List<Plant>
)