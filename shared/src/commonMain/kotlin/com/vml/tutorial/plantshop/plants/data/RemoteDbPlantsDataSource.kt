package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.core.utils.exts.getCollection
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where

class RemoteDbPlantsDataSource : PlantsDataSource {
    private val collection = Firebase.firestore.collection("plants")

    override suspend fun getPlants(): List<Plant>? {
        return collection.getCollection()
    }

    override suspend fun getPlant(id: Int): Plant? {
        return collection.where("id",id).getCollection<Plant>().firstOrNull()
    }

    override suspend fun addToPlants(plant: Plant) {
        collection.add(plant)
    }

    override suspend fun getPlantCount(): Int {
        return getPlants()?.count() ?: -1
    }
}
