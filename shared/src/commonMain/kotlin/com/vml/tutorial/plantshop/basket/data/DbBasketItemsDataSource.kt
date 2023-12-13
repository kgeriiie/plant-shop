package com.vml.tutorial.plantshop.basket.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.vml.tutorial.plantshop.PlantDatabase
import com.vml.tutorial.plantshop.basket.domain.BasketDataSource
import com.vml.tutorial.plantshop.basket.domain.BasketItem
import database.BasketQueries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DbBasketItemsDataSource(
    db: PlantDatabase,
): BasketDataSource {
    private val queries: BasketQueries = db.basketQueries
    override fun getBasketItems(): Flow<List<BasketItem>> {
        return queries
            .getBasketItems()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { entities ->
                entities.map { it.toBasketItem() }
            }
    }

    override suspend fun insertItem(basketItem: BasketItem) {
        queries.insertBasketItem(null, basketItem.plantId.toLong(), basketItem.quantity.toLong())
    }

    override suspend fun updateQuantity(plantId: Int, quantity: Int) {
        queries.updateQuantity(quantity.toLong(), plantId.toLong())
    }

    override suspend fun deleteItem(plantId: Int) {
        queries.deleteBasketItem(plantId.toLong())
    }

    override suspend fun deleteAll() {
        queries.deleteBasketItems()
    }
}