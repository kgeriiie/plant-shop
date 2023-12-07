package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource

interface PlantsRepository {
    fun getPlants(): List<Plant>
}

class PlantsRepositoryImpl(
    private val localDataSource: PlantsDataSource
): PlantsRepository {
    override fun getPlants(): List<Plant> {
        return localDataSource.getPlants()
    }
}