package com.vml.tutorial.plantshop.plants.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.vml.tutorial.plantshop.PlantDatabase
import com.vml.tutorial.plantshop.plants.domain.FavoritesDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class DbFavoritesDataSource(db: PlantDatabase) : FavoritesDataSource {
    private val queries = db.favoritesQueries
    override fun getIds(): Flow<List<Long>> {
        return queries.getFavoritePlants()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    override suspend fun insertToDatabase(id: Int) {
        queries.insertFavoritePlant(id.toLong())
    }

    override suspend fun removeFromDatabase(id: Int) {
        queries.removeFavoritePlant(id.toLong())
    }

    override suspend fun isIdInDatabase(id: Int): Boolean {
        try {
            queries.getFavoritePlantById(id.toLong()).executeAsOne()
        } catch (err: NullPointerException) {
            return false
        }
        return true
    }
}
