package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.core.utils.exts.orInvalid
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class RemoteDbPlantsDataSource : PlantsDataSource {
    private val collection = Firebase.firestore.collection("plants")

    override suspend fun getPlants(): List<Plant>? {
        return try {
            collection.get().documents.map { it.data() }
        } catch (err: Exception) {
            null
        }
    }

    override suspend fun addToPlants(plant: Plant) {
        collection.add(plant)
    }

    override suspend fun getPlantCount(): Int {
        return getPlants()?.count().orInvalid()
    }
}
