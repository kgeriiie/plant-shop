package com.vml.tutorial.plantshop.helpers

import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantDetails
import com.vml.tutorial.plantshop.plants.presentation.detail.PlantDetailState

fun createPlant(
    id: Int = 1,
    name: String = "Test Plant",
    originalName: String = "Test Plant Original Name",
    price: Double = 1.0,
    currency: String = "$",
    image: String = "",
    types: String = "",
    description: String = "Some description",
    detailsSize: String = "1x1",
    detailsTemperature: String = "20",
    detailsFullSun: Boolean = true,
    detailsDrained: Boolean = true
) = Plant(
    id = id,
    name = name,
    originalName = originalName,
    price = price,
    currency = currency,
    image = image,
    types = types,
    description = description,
    details = PlantDetails(
        size = detailsSize,
        temperature = detailsTemperature,
        fullSun = detailsFullSun,
        drained = detailsDrained
    )
)

fun createPlantDetailState(
    plant: Plant = createPlant(),
    isFavourite: Boolean = false,
    quantity: Int = 1,
    showAddToBasketDialog: Boolean = false
) = PlantDetailState(
    plant = plant,
    isFavourite = isFavourite,
    quantity = quantity,
    showAddToBasketDialog = showAddToBasketDialog
)