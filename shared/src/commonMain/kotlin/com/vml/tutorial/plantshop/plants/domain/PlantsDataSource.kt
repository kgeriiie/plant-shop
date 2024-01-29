package com.vml.tutorial.plantshop.plants.domain

interface PlantsDataSource {
    suspend fun getPlants(): List<Plant>
    suspend fun getPlant(id: Int): Plant?
    suspend fun addToPlants(plant: Plant)
    suspend fun getPlantCount(): Int
}
