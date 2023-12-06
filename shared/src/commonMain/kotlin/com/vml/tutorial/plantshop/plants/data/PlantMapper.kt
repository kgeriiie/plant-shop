package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantDetails
import database.FavoritePlant

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
