package com.vml.tutorial.plantshop.di

import com.vml.tutorial.plantshop.PlantDatabase
import com.vml.tutorial.plantshop.core.data.DatabaseDriverFactory
import com.vml.tutorial.plantshop.core.data.FileReader
import com.vml.tutorial.plantshop.plants.data.DbPlantsDataSource
import com.vml.tutorial.plantshop.plants.data.FilePlantsDataSource
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.data.PlantsRepositoryImpl
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource
import kotlinx.coroutines.CoroutineScope

actual class AppModule (private val lifecycleScope: CoroutineScope) {
    actual val plantsDataSource: PlantsDataSource by lazy {
        FilePlantsDataSource(FileReader())
    }
    actual val plantsRepository: PlantsRepository by lazy {
        PlantsRepositoryImpl(
            plantsDataSource, DbPlantsDataSource(
                db = PlantDatabase(
                    driver = DatabaseDriverFactory().create()
                )
            ), lifecycleScope
        )
    }
}
