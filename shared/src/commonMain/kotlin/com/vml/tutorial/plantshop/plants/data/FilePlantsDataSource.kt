package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.MR.files.plants
import com.vml.tutorial.plantshop.PlantDatabase
import com.vml.tutorial.plantshop.core.data.FileReader
import com.vml.tutorial.plantshop.core.data.toPlant
import com.vml.tutorial.plantshop.core.data.toPlants
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource

class FilePlantsDataSource(
    private val fileReader: FileReader,
    db: PlantDatabase
) : PlantsDataSource {
    private val queries = db.plantQueries

    override fun getPlants(): List<Plant> {
        val rawData: String = fileReader.loadFile(plants) ?: return emptyList()
        return getPlantsWithFavoriteState(getFavorites(), rawData.toPlants())
    }

    override fun getFavorites(): List<Plant> {
        return queries.getFavoritePlants().executeAsList().map { plantEntity ->
            plantEntity.toPlant()
        }
    }

    private fun getPlantsWithFavoriteState(
        favorites: List<Plant>,
        plants: List<Plant>
    ): List<Plant> {
        for (favorite in favorites) {
            val matchingPlant = plants.find { it.id == favorite.id }
            matchingPlant?.isFavorite = true
        }
        return plants
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

    override suspend fun getFavoritePlantById(id: Int): Plant {
        return queries.getFavoritePlantById(id.toLong()).executeAsOne().toPlant()
    }
}
