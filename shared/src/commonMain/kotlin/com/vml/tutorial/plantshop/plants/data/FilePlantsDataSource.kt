package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.MR.files.plants
import com.vml.tutorial.plantshop.core.data.FileReader
import com.vml.tutorial.plantshop.core.data.toPlants
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource

class FilePlantsDataSource(private val fileReader: FileReader) : PlantsDataSource {
    override fun getPlants(): List<Plant> {
        val rawData: String = fileReader.loadFile(plants) ?: return emptyList()
        return rawData.toPlants()
    }
}
