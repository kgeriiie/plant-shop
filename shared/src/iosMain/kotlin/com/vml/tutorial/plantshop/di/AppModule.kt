package com.vml.tutorial.plantshop.di

import com.vml.tutorial.plantshop.core.data.FileReader
import com.vml.tutorial.plantshop.plants.data.FilePlantsDataSource
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource

actual class AppModule() {
    actual val plantsDataSource: PlantsDataSource by lazy {
        FilePlantsDataSource(FileReader())
    }
}