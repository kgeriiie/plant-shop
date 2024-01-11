package com.vml.tutorial.plantshop.plants.data

import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class DbPlantsDataSource : PlantsDataSource {
    override suspend fun getPlants(): List<Plant>? {
        return try {
            val userResponse = Firebase.firestore.collection("plants").get()
            userResponse.documents.map { it.data() }
        } catch (err: Exception) {
            null
        }
    }
}
