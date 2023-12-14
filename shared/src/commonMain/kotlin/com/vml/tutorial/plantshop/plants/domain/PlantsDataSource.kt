package com.vml.tutorial.plantshop.plants.domain

import kotlinx.coroutines.flow.Flow

interface PlantsDataSource {
    fun getPlants(): Flow<List<Plant>>
}
