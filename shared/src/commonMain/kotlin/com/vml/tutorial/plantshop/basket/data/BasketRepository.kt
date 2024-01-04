package com.vml.tutorial.plantshop.basket.data

import com.vml.tutorial.plantshop.basket.domain.BasketDataSource
import com.vml.tutorial.plantshop.basket.domain.BasketItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

interface BasketRepository {
    val basketItemsFlow: StateFlow<List<BasketItem>>
    suspend fun getBasketItems(): List<BasketItem>
    suspend fun insertItem(plantId: Int, quantity: Int)
    suspend fun updateItemQuantity(plantId: Int, quantity: Int)
    suspend fun deleteItem(plantId: Int)
    suspend fun deleteAll()
}

class BasketRepositoryImpl(
    private val basketDataSource: BasketDataSource
): BasketRepository {
    private val _basketItemsFlow: MutableStateFlow<List<BasketItem>> = MutableStateFlow(listOf())
    override val basketItemsFlow: StateFlow<List<BasketItem>>
        get() = _basketItemsFlow.asStateFlow()

    override suspend fun getBasketItems(): List<BasketItem> {
        return basketDataSource.getBasketItems().first().also {
            _basketItemsFlow.emit(it)
        }
    }

    override suspend fun insertItem(plantId: Int, quantity: Int) {
        getBasketItems().firstOrNull { it.plantId == plantId }?.let { item ->
            updateItemQuantity(plantId, item.quantity.plus(quantity))
        }?: basketDataSource.insertItem(BasketItem(plantId, quantity)).also { getBasketItems() }
    }

    override suspend fun updateItemQuantity(plantId: Int, quantity: Int) {
        basketDataSource.updateQuantity(plantId, quantity).also { getBasketItems() }
    }

    override suspend fun deleteItem(plantId: Int) {
        basketDataSource.deleteItem(plantId).also { getBasketItems() }
    }

    override suspend fun deleteAll() {
        basketDataSource.deleteAll().also { getBasketItems() }
    }
}
