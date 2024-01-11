package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class DbPlantsDataSource : PlantsDataSource {
    override suspend fun getPlants(): List<Plant>? {
        return try {
            val response = Firebase.firestore.collection("plants").get()
            response.documents.map { it.data() }
        } catch (err: Exception) {
            null
        }
    }
}
