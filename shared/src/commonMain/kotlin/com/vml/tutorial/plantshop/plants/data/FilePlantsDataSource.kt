package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.MR.files.plants
import com.vml.tutorial.plantshop.core.data.FileReader
import com.vml.tutorial.plantshop.core.data.toPlants
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FilePlantsDataSource(private val fileReader: FileReader) : PlantsDataSource {
    override fun getPlants(): Flow<List<Plant>> {
        val rawData: String =
            fileReader.loadFile(plants) ?: return flow { emit(emptyList<Plant>()) }
        return flow { emit(rawData.toPlants()) }
    }
}
