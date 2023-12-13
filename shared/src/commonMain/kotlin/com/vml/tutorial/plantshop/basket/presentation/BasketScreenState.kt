package com.vml.tutorial.plantshop.basket.presentation

import com.vml.tutorial.plantshop.core.utils.exts.orFalse
import com.vml.tutorial.plantshop.core.utils.exts.orZero
import com.vml.tutorial.plantshop.core.utils.exts.roundTo
import com.vml.tutorial.plantshop.plants.domain.Plant

data class BasketScreenState(
    val items: List<BasketItemState>? = null,
    val discount: Double = 3.0   // 3 USD
) {
    private val itemsPrice: Double get() = items.orEmpty().sumOf { it.totalPrice }
    private val totalPrice: Double get() = itemsPrice.minus(discount).takeUnless { items.isNullOrEmpty() }.orZero()
    val discountText: String get() = "-$ ${discount.roundTo(numFractionDigits = 2)}"
    val itemsPriceText: String get() = "$ ${itemsPrice.roundTo(numFractionDigits = 2)}"
    val totalPriceText: String get() = "$ ${totalPrice.roundTo(numFractionDigits = 2)}"

    val displayEmptyMessage: Boolean get() = items?.isEmpty().orFalse()
}

data class BasketItemState(
    val plant: Plant,
    val quantity: Int
) {
    val totalPrice: Double get() = plant.price.times(quantity)
}