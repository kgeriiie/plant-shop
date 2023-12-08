package com.vml.tutorial.plantshop.plants.domain

interface PlantsDataSource {
    fun getPlants(): List<Plant>

    fun getFavorites(): List<Plant>
    suspend fun addToFavorites(plant: Plant)
    suspend fun removeFromFavorites(id: Int)
    suspend fun getFavoritePlantById(id: Int): Plant
}
