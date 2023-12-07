package com.vml.tutorial.plantshop.di

import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource

expect class AppModule {
    val plantsDataSource: PlantsDataSource
    val plantsRepository: PlantsRepository
}