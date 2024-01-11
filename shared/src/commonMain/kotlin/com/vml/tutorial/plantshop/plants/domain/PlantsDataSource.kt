package com.vml.tutorial.plantshop.plants.domain

interface PlantsDataSource {
    suspend fun getPlants(): List<Plant>?
}
