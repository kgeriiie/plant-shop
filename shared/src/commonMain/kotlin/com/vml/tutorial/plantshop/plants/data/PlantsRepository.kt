package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.plants.domain.DbDataSource
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

interface PlantsRepository {
    suspend fun getPlants(): Flow<List<Plant>>
    suspend fun getFavorites(): Flow<List<Plant>>
    suspend fun toggleFavoriteStatus(plantId: Int)
}

class PlantsRepositoryImpl(
    private val localDataSource: PlantsDataSource,
    private val dbDataSource: DbDataSource
) : PlantsRepository {
    override suspend fun getPlants(): Flow<List<Plant>> {
        return getPlantsWithFavoriteState(localDataSource.getPlants())
    }

    private fun getPlantsWithFavoriteState(plants: Flow<List<Plant>>): Flow<List<Plant>> {
        return combine(plants, dbDataSource.getIds()) { plantList, favoriteIdsList ->
            plantList.map { plant ->
                plant.copy(isFavorite = favoriteIdsList.contains(plant.id.toLong()))
            }
        }
    }

    override suspend fun getFavorites(): Flow<List<Plant>> {
        return combine(getPlants(), dbDataSource.getIds()) { plantList, favoriteIds ->
            plantList.filter { plant ->
                favoriteIds.contains(plant.id.toLong())
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
