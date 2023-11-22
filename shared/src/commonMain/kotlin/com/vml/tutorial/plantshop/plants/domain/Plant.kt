package com.vml.tutorial.plantshop.plants.domain

import kotlinx.serialization.Serializable

@Serializable
data class Plant(
    val id: Int,
    val name: String,
    val originalName: String,
    val price: String,
    val image: String,
    val types: String,
    val description: String,
    val details: PlantDetails
)

@Serializable
data class PlantDetails(
    val size: String,
    val temperature: String,
    val fullSun: Boolean,
    val drained: Boolean
)
