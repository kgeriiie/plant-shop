package com.vml.tutorial.plantshop.profile.orders.domain

interface OrdersDataSource {
    suspend fun fetchOrders(userId: String): List<OrderItem>

    suspend fun insertOrder(userId: String, order: OrderItem)
}