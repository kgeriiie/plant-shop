package com.vml.tutorial.plantshop.plants.domain

import kotlinx.coroutines.flow.Flow

interface PlantsDataSource {
    fun getPlants(): List<Plant>

    fun getFavorites(): Flow<List<Plant>>
    suspend fun addToFavorites(plant: Plant)
    suspend fun removeFromFavorites(id: Int)
}
