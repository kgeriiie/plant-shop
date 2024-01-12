package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.PlantDatabase
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantDetails
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource

class DbPlantsDataSource(db: PlantDatabase) : PlantsDataSource {
    private val queries = db.plantsQueries
    override suspend fun getPlants(): List<Plant> {
        return queries.getPlants().executeAsList().map { plantEntity ->
            Plant(
                id = plantEntity.id.toInt(),
                name = plantEntity.name,
                originalName = plantEntity.originalName,
                price = plantEntity.price.toDouble(),
                currency = plantEntity.currency,
                image = plantEntity.image,
                types = plantEntity.types,
                description = plantEntity.description,
                details = PlantDetails(
                    size = plantEntity.size,
                    temperature = plantEntity.temperature,
                    fullSun = plantEntity.fullSun,
                    drained = plantEntity.drained
                )
            )
        }
    }

    override suspend fun addToPlants(plant: Plant) {
        queries.insertPlant(
            id = plant.id.toLong(),
            name = plant.name,
            originalName = plant.originalName,
            price = plant.price.toString(),
            image = plant.image,
            types = plant.types,
            description = plant.description,
            currency = plant.currency,
            size = plant.details.size,
            temperature = plant.details.temperature,
            fullSun = plant.details.fullSun,
            drained = plant.details.drained
        )
    }

    override suspend fun getPlantCount(): Int {
        return queries.getPlantCount().executeAsOne().toInt()
    }
}
