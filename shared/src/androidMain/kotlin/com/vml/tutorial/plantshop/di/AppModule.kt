package com.vml.tutorial.plantshop.di

import android.content.Context
import com.vml.tutorial.plantshop.PlantDatabase
import com.vml.tutorial.plantshop.core.data.DatabaseDriverFactory
import com.vml.tutorial.plantshop.core.data.FileReader
import com.vml.tutorial.plantshop.plants.data.DbPlantsDataSource
import com.vml.tutorial.plantshop.plants.data.FilePlantsDataSource
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.data.PlantsRepositoryImpl
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource

actual class AppModule(private val context: Context) {
    actual val plantsDataSource: PlantsDataSource by lazy {
        FilePlantsDataSource(FileReader(context))
    }
    actual val dbPlantsDataSource: DbPlantsDataSource by lazy {
        DbPlantsDataSource(
            db = PlantDatabase(
                driver = DatabaseDriverFactory(context).create()
            )
        )
    }
    actual val plantsRepository: PlantsRepository by lazy {
        PlantsRepositoryImpl(plantsDataSource, dbPlantsDataSource)
    }
}
