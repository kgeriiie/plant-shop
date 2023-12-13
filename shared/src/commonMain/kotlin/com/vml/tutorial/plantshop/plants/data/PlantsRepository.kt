package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.core.data.toPlant
import com.vml.tutorial.plantshop.core.utils.Logger
import com.vml.tutorial.plantshop.plants.domain.DbDataSource
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope

interface PlantsRepository {
    fun getPlants(): List<Plant>
    fun getFavorites(): Flow<List<Plant>>
    suspend fun toggleFavoriteStatus(plantId: Int)
}

class PlantsRepositoryImpl(
    private val localDataSource: PlantsDataSource,
    private val dbDataSource: DbDataSource
) : PlantsRepository {
    override fun getPlants(): List<Plant> {
        Logger.d("test--", "getPlants called")
        return localDataSource.getPlants(dbDataSource.getIds())
    }

    override fun getFavorites(): Flow<List<Plant>> {
        val plants = getPlants()
        return dbDataSource.getIds().map { plantEntities ->
            supervisorScope {
                plantEntities.map {
                    async { it.toPlant(plants) }
                }.map {
                    it.await()
                }
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
