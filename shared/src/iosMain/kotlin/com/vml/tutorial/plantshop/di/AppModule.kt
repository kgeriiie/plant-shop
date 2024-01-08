package com.vml.tutorial.plantshop.di

import com.vml.tutorial.plantshop.PlantDatabase
import com.vml.tutorial.plantshop.basket.data.BasketRepository
import com.vml.tutorial.plantshop.basket.data.BasketRepositoryImpl
import com.vml.tutorial.plantshop.basket.data.DbBasketItemsDataSource
import com.vml.tutorial.plantshop.core.data.DatabaseDriverFactory
import com.vml.tutorial.plantshop.core.data.FileReader
import com.vml.tutorial.plantshop.core.utils.ShareUtils
import com.vml.tutorial.plantshop.plants.data.DbPlantsDataSource
import com.vml.tutorial.plantshop.plants.data.FilePlantsDataSource
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.data.PlantsRepositoryImpl
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource

actual class AppModule {
    private val db: PlantDatabase by lazy {
        PlantDatabase(
            driver = DatabaseDriverFactory().create()
        )
    }

    actual val plantsDataSource: PlantsDataSource by lazy {
        FilePlantsDataSource(FileReader())
    }

    actual val dbPlantsDataSource: DbPlantsDataSource
        get() = DbPlantsDataSource(db)

    actual val plantsRepository: PlantsRepository by lazy {
        PlantsRepositoryImpl(
            plantsDataSource,
            dbPlantsDataSource
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
