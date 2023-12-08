package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource

interface PlantsRepository {
    fun getPlants(): List<Plant>
    suspend fun getFavorites(): List<Plant>
    suspend fun addToFavorites(plant: Plant)
    suspend fun removeFromFavorites(id: Int)
    suspend fun getFavoritePlantById(id: Int): Plant
}

class PlantsRepositoryImpl(
    private val localDataSource: PlantsDataSource
): PlantsRepository {
    override fun getPlants(): List<Plant> {
        return localDataSource.getPlants()
    }
    override suspend fun getFavorites(): List<Plant> {
        return localDataSource.getFavorites()
    }

    override suspend fun addToFavorites(plant: Plant) {
        return localDataSource.addToFavorites(plant)
    }

    override suspend fun removeFromFavorites(id: Int) {
        return localDataSource.removeFromFavorites(id)
    }

    override suspend fun getFavoritePlantById(id: Int): Plant {
        return localDataSource.getFavoritePlantById(id)
    }
}