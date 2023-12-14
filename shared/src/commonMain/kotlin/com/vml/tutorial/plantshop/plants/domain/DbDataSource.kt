package com.vml.tutorial.plantshop.plants.domain

import kotlinx.coroutines.flow.Flow

interface DbDataSource {
    suspend fun insertToDatabase(id: Int)
    suspend fun removeFromDatabase(id: Int)
    suspend fun isIdInDatabase(id: Int): Boolean
    fun getIds(): Flow<List<Long>>
}