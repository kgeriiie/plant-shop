package com.vml.tutorial.plantshop.plants.domain

import kotlinx.serialization.Serializable

@Serializable
data class Plant(
    val id: Int,
    val name: String,
    val originalName: String,
    val price: Double,
    val currency: String,
    val image: String,
    val types: String,
    val description: String,
    val details: PlantDetails,
    var isFavorite: Boolean = false
) {
    val priceText: String get() {
        return "$currency $price"
    }
}

@Serializable
data class PlantDetails(
    val size: String,
    val temperature: String,
    val fullSun: Boolean,
    val drained: Boolean
)
