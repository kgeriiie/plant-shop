package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.core.utils.Logger
import com.vml.tutorial.plantshop.plants.domain.DbDataSource
import com.vml.tutorial.plantshop.plants.domain.FavoritesDataSource
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

interface PlantsRepository {
    fun getPlants(): List<Plant>
    fun getFavorites(): Flow<List<Plant>>
    fun toggleFavoriteStatus(plantId: Int)
}

class PlantsRepositoryImpl(
    private val localDataSource: PlantsDataSource,
    private val dbDataSource: DbDataSource,
    private val favoritesDataSource: FavoritesDataSource
) : PlantsRepository {
    override fun getPlants(): List<Plant> {
        Logger.d("test--","getPlants called")
        return localDataSource.getPlants(dbDataSource.getIds())
    }

    override fun getFavorites(): Flow<List<Plant>> {
        return favoritesDataSource.getFavorites(dbDataSource.getIds(), getPlants())
    }

    override fun toggleFavoriteStatus(plantId: Int) {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            if (dbDataSource.isIdInDatabase(plantId)) {
                dbDataSource.removeFromDatabase(plantId)
            } else {
                dbDataSource.insertToDatabase(plantId)
            }
        }
    }
}
