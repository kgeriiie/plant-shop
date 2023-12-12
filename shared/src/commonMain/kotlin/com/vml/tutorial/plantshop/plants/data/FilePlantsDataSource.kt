package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.MR.files.plants
import com.vml.tutorial.plantshop.core.data.FileReader
import com.vml.tutorial.plantshop.core.data.toPlants
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FilePlantsDataSource(private val fileReader: FileReader) : PlantsDataSource {
    override fun getPlants(favoriteIds: Flow<List<Long>>): List<Plant> {
        val rawData: String = fileReader.loadFile(plants) ?: return emptyList()
        return getPlantsWithFavoriteState(rawData.toPlants(), favoriteIds)
    }

    private fun getPlantsWithFavoriteState(
        plants: List<Plant>,
        favoriteIds: Flow<List<Long>>
    ): List<Plant> {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            favoriteIds.collect { favoritePlantIds ->
                plants.map { plant ->
                    plant.isFavorite = favoritePlantIds.any { it == plant.id.toLong() }
                }
            }
        }
        return plants
    }
}
