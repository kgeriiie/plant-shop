package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.plants.domain.FavoritesDataSource
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource
import com.vml.tutorial.plantshop.plants.presentation.PlantCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface PlantsRepository {
    fun getPlants(category: PlantCategory): List<Plant>
    fun getFavorites(): Flow<List<Plant>>
    suspend fun toggleFavoriteStatus(plantId: Int)
}

class PlantsRepositoryImpl(
    private val localDataSource: PlantsDataSource,
    private val favoritesDataSource: FavoritesDataSource
) : PlantsRepository {
    override fun getPlants(category: PlantCategory): List<Plant> {
        return localDataSource.getPlants().filter { plant ->
            plant.types.contains(category.type)
        }
    }

    override fun getFavorites(): Flow<List<Plant>> {
        return favoritesDataSource.getIds().map { favouriteIds ->
            favouriteIds.mapNotNull { id ->
                getPlants(PlantCategory.NONE)
                    .firstOrNull { it.id.toLong() == id }
                    ?.copy(isFavorite = true)
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
