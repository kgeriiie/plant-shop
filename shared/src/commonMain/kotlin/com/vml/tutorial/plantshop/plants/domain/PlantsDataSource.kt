package com.vml.tutorial.plantshop.plants.domain

import com.vml.tutorial.plantshop.plants.domain.Plant

interface PlantsDataSource {
    fun getPlants(): List<Plant>
}