package com.vml.tutorial.plantshop.di

import com.vml.tutorial.plantshop.basket.data.BasketRepository
import com.vml.tutorial.plantshop.core.utils.ShareUtils
import com.vml.tutorial.plantshop.plants.data.DbFavoritesDataSource
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource

expect class AppModule {
    val dbPlantsDataSource: PlantsDataSource
    val remoteDbPlantsDataSource: PlantsDataSource
    val plantsRepository: PlantsRepository
    val dbFavoritesDataSource: DbFavoritesDataSource
    val basketRepository: BasketRepository
    val shareUtils: ShareUtils
}
