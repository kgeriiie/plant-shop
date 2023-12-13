package com.vml.tutorial.plantshop.basket.domain

import kotlinx.coroutines.flow.Flow

interface BasketDataSource {
    fun getBasketItems(): Flow<List<BasketItem>>
    suspend fun insertItem(basketItem: BasketItem)
    suspend fun updateQuantity(plantId: Int, quantity: Int)
    suspend fun deleteItem(plantId: Int)
    suspend fun deleteAll()
}