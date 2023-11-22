package com.vml.tutorial.plantshop.core.data

import com.vml.tutorial.plantshop.plants.domain.Plant
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