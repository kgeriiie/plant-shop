package com.vml.tutorial.plantshop.core.data

import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantDetails
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

fun Long.toPlant(plants: List<Plant>): Plant {
    return plants.find {
        it.id == this.toInt()
    } ?: run {
        Plant(
            -1,
            "",
            "",
            "",
            "",
            "",
            "",
            PlantDetails("", "", fullSun = false, drained = false),
            false
        )
    }
}
