package com.vml.tutorial.plantshop.di

import android.content.Context
import com.vml.tutorial.plantshop.PlantDatabase
import com.vml.tutorial.plantshop.core.data.DatabaseDriverFactory
import com.vml.tutorial.plantshop.core.data.FileReader
import com.vml.tutorial.plantshop.plants.data.DbPlantsDataSource
import com.vml.tutorial.plantshop.plants.data.FavoritePlantsDataSource
import com.vml.tutorial.plantshop.plants.data.FilePlantsDataSource
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.data.PlantsRepositoryImpl
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource

actual class AppModule(private val context: Context) {
    actual val plantsDataSource: PlantsDataSource by lazy {
        FilePlantsDataSource(fileReader = FileReader(context))
    }

    actual val plantsRepository: PlantsRepository by lazy {
        PlantsRepositoryImpl(
            plantsDataSource,
            DbPlantsDataSource(
                db = PlantDatabase(
                    driver = DatabaseDriverFactory(context).create()
                )
            ), FavoritePlantsDataSource()
        )
    }
}
