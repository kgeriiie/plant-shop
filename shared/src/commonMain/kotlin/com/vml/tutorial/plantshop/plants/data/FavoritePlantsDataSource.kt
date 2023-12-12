package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.core.data.toPlant
import com.vml.tutorial.plantshop.plants.domain.FavoritesDataSource
import com.vml.tutorial.plantshop.plants.domain.Plant
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope

class FavoritePlantsDataSource : FavoritesDataSource {
    override fun getFavorites(
        favoriteIds: Flow<List<Long>>,
        plants: List<Plant>
    ): Flow<List<Plant>> {
        return favoriteIds.map { plantEntities ->
            supervisorScope {
                plantEntities.map {
                    async { it.toPlant(plants) }
                }.map { it.await() }
            }
        }
    }
}