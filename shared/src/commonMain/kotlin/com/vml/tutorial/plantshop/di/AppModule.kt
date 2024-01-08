package com.vml.tutorial.plantshop.di

import com.vml.tutorial.plantshop.basket.data.BasketRepository
import com.vml.tutorial.plantshop.core.utils.ShareUtils
import com.vml.tutorial.plantshop.plants.data.DbPlantsDataSource
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource

expect class AppModule {
    val plantsDataSource: PlantsDataSource
    val plantsRepository: PlantsRepository
    val dbPlantsDataSource: DbPlantsDataSource
    val basketRepository: BasketRepository
    val shareUtils: ShareUtils
}
