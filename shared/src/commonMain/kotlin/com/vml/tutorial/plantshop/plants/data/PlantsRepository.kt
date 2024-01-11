package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.plants.domain.FavoritesDataSource
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PlantsRepository {
    suspend fun getPlants(): List<Plant>?
    fun getFavorites(): Flow<List<Plant>>
    suspend fun toggleFavoriteStatus(plantId: Int)
}

class PlantsRepositoryImpl(
    private val dbPlantsDataSource: PlantsDataSource,
    private val favoritesDataSource: FavoritesDataSource
) : PlantsRepository {
    override suspend fun getPlants(): List<Plant>? {
        return dbPlantsDataSource.getPlants()
    }

    override fun getFavorites(): Flow<List<Plant>> {
        return favoritesDataSource.getIds().map { favouriteIds ->
            favouriteIds.mapNotNull { id ->
                getPlants()?.firstOrNull { it.id.toLong() == id }?.copy(isFavorite = true)
            }
        }
    }

    override suspend fun toggleFavoriteStatus(plantId: Int) {
        if (favoritesDataSource.isIdInDatabase(plantId)) {
            favoritesDataSource.removeFromDatabase(plantId)
        } else {
            favoritesDataSource.insertToDatabase(plantId)
        }
    }
}
