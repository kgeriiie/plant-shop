package com.vml.tutorial.plantshop.core.data

import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantDetails
import database.FavoritePlant
import kotlinx.serialization.json.Json

fun String.toPlant(): Plant? {
    return try {
        Json.decodeFromString<Plant>(this)
    } catch (e: Exception) {
        null
    }
}

fun String.toPlants(): List<Plant> {
    return try {
        Json.decodeFromString(this)
    } catch (e: Exception) {
        return emptyList()
    }
}

fun FavoritePlant.toPlant(): Plant {
    return Plant(
        id = id.toInt(),
        name = name,
        originalName = originalName,
        price = price,
        image = image,
        types = types,
        description = description,
        details = PlantDetails(
            size = size,
            temperature = temperature,
            fullSun = fullSun,
            drained = drained
        )
    )
}
