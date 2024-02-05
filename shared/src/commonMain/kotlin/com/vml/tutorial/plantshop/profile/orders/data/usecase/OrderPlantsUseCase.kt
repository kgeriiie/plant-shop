package com.vml.tutorial.plantshop.profile.orders.data.usecase

import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.profile.orders.data.OrdersRepository

class OrderPlantsUseCase(
    private val ordersRepository: OrdersRepository,
) {
    suspend operator fun invoke(plants: List<Plant>, discount: Double = 0.0): Boolean {
        if (plants.isEmpty()) return false
        return ordersRepository.createAnOrder(
            itemIds = plants.map { it.id },
            totalPrice = plants.sumOf { it.price }.minus(discount),
            currency = plants.first().currency
        )
    }
}