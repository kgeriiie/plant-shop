package com.vml.tutorial.plantshop.di

import com.vml.tutorial.plantshop.PlantDatabase
import com.vml.tutorial.plantshop.basket.data.BasketRepository
import com.vml.tutorial.plantshop.basket.data.BasketRepositoryImpl
import com.vml.tutorial.plantshop.basket.data.DbBasketItemsDataSource
import com.vml.tutorial.plantshop.core.data.DatabaseDriverFactory
import com.vml.tutorial.plantshop.core.utils.ShareUtils
import com.vml.tutorial.plantshop.plants.data.DbFavoritesDataSource
import com.vml.tutorial.plantshop.plants.data.DbPlantsDataSource
import com.vml.tutorial.plantshop.plants.data.RemoteDbPlantsDataSource
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.data.PlantsRepositoryImpl
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource

actual class AppModule {
    private val db: PlantDatabase by lazy {
        PlantDatabase(
            driver = DatabaseDriverFactory().create()
        )
    }

    actual val dbFavoritesDataSource: DbFavoritesDataSource by lazy {
        DbFavoritesDataSource(db)
    }

    actual val remoteDbPlantsDataSource: PlantsDataSource by lazy {
        RemoteDbPlantsDataSource()
    }

    actual val dbPlantsDataSource: PlantsDataSource by lazy {
        DbPlantsDataSource(db)
    }

    actual val plantsRepository: PlantsRepository by lazy {
        PlantsRepositoryImpl(
            dbPlantsDataSource,
            remoteDbPlantsDataSource,
            dbFavoritesDataSource
        )
    }
    actual val basketRepository: BasketRepository by lazy {
        BasketRepositoryImpl(
            DbBasketItemsDataSource(db)
        )
    }
    actual val shareUtils: ShareUtils by lazy {
        ShareUtils()
    }
}
