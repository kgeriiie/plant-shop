package com.vml.tutorial.plantshop.plants.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.vml.tutorial.plantshop.MR.files.plants
import com.vml.tutorial.plantshop.PlantDatabase
import com.vml.tutorial.plantshop.core.data.FileReader
import com.vml.tutorial.plantshop.core.data.toPlants
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FilePlantsDataSource(
    private val fileReader: FileReader,
    db: PlantDatabase
) : PlantsDataSource {
    private val queries = db.plantQueries

    override fun getPlants(): List<Plant> {
        val rawData: String = fileReader.loadFile(plants) ?: return emptyList()
        return rawData.toPlants()
    }

    override fun getFavorites(): Flow<List<Plant>> {
        return queries.getFavoritePlants()
            .asFlow().mapToList(Dispatchers.Default)
            .map { plantEntities ->
                plantEntities.map { plantEntity ->
                    plantEntity.toPlant()
                }
            }
    }

    override suspend fun addToFavorites(plant: Plant) {
        queries.insertFavoritePlant(
            id = plant.id.toLong(),
            name = plant.name,
            originalName = plant.originalName,
            price = plant.price,
            image = plant.image,
            types = plant.types,
            description = plant.description,
            size = plant.details.size,
            temperature = plant.details.temperature,
            fullSun = plant.details.fullSun,
            drained = plant.details.drained
        )
    }

    override suspend fun removeFromFavorites(id: Int) {
        queries.removeFavoritePlant(id.toLong())
    }
}
