package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.core.utils.Logger
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

interface PlantsRepository {
    fun getPlants(): List<Plant>
    fun getFavorites(): List<Plant>
    fun toggleFavoriteStatus(plant: Plant)
}

class PlantsRepositoryImpl(
    private val localDataSource: PlantsDataSource
) : PlantsRepository {
    override fun getPlants(): List<Plant> {
        Logger.d("test--","getPlants called")
        return localDataSource.getPlants()
    }

    override fun getFavorites(): List<Plant> {
        return localDataSource.getFavorites()
    }

    override fun toggleFavoriteStatus(plant: Plant) {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            val chosenPlant = try {
                localDataSource.getFavoritePlantById(plant.id)
            } catch (err: NullPointerException) {
                null
            }
            chosenPlant?.let {
                localDataSource.removeFromFavorites(it.id)
            } ?: run {
                localDataSource.addToFavorites(plant)
            }
        }
    }
}
