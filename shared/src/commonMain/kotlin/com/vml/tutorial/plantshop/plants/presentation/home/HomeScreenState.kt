package com.vml.tutorial.plantshop.plants.presentation.home

import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.PlantCategory

data class HomeScreenState(
    val plants: List<Plant> = emptyList(),
    val chosenCategory: PlantCategory = PlantCategory.NONE,
    val searchResults: List<Plant> = emptyList()
)
