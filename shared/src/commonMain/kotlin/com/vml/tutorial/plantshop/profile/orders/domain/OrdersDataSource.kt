package com.vml.tutorial.plantshop.profile.orders.domain

interface OrdersDataSource {
    suspend fun fetchOrders(userId: String): List<OrderItem>
    suspend fun updateOrder(order: OrderItem)
    suspend fun insertOrder(order: OrderItem)
}