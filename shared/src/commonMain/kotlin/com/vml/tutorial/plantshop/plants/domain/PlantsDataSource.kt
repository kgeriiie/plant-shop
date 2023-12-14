package com.vml.tutorial.plantshop.plants.domain

interface PlantsDataSource {
    fun getPlants(): List<Plant>
}
