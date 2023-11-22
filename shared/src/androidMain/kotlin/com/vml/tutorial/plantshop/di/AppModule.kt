package com.vml.tutorial.plantshop.di

import android.content.Context
import com.vml.tutorial.plantshop.core.data.FileReader
import com.vml.tutorial.plantshop.plants.data.FilePlantsDataSource
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource

actual class AppModule(private val context: Context) {
    actual val plantsDataSource: PlantsDataSource by lazy {
        FilePlantsDataSource(FileReader(context))
    }
}