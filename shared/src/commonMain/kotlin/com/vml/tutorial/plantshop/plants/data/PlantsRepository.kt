package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.core.utils.Logger
import com.vml.tutorial.plantshop.plants.domain.DbDataSource
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

interface PlantsRepository {
    fun getPlants(): List<Plant>
    fun getFavorites(): Flow<List<Plant>>
    suspend fun toggleFavoriteStatus(plantId: Int)
}

class PlantsRepositoryImpl(
    private val localDataSource: PlantsDataSource,
    private val dbDataSource: DbDataSource,
    private val coroutineScope: CoroutineScope
) : PlantsRepository {
    override fun getPlants(): List<Plant> {
        Logger.d("test--", "getPlants called")
        return getPlantsWithFavoriteState(localDataSource.getPlants())
    }

    private fun getPlantsWithFavoriteState(plants: List<Plant>): List<Plant> {
        coroutineScope.launch {
            dbDataSource.getIds().collect { favoritePlantIds ->
                plants.map { plant ->
                    plant.isFavorite = favoritePlantIds.any { it == plant.id.toLong() }
                }
            }
        }
        return plants
    }

    override fun getFavorites(): Flow<List<Plant>> {
        val plants = getPlants()
        return dbDataSource.getIds().map { ids ->
            ids.mapNotNull { id ->
                plants.firstOrNull { it.id.toLong() == id }
            }
        }
    }

    override suspend fun toggleFavoriteStatus(plantId: Int) {
        if (dbDataSource.isIdInDatabase(plantId)) {
            dbDataSource.removeFromDatabase(plantId)
        } else {
            dbDataSource.insertToDatabase(plantId)
        }
    }
}
