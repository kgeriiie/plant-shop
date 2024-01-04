package com.vml.tutorial.plantshop.basket.data

import com.vml.tutorial.plantshop.basket.domain.BasketItem
import database.BasketEntity

fun BasketEntity.toBasketItem(): BasketItem {
    return BasketItem(
        plantId = plantId.toInt(),
        quantity = quantity.toInt()
    )
}