package com.vml.tutorial.plantshop.profile.orders.domain

interface OrdersDataSource {
    suspend fun fetchOrders(): List<OrderItem>

    suspend fun insertOrder(order: OrderItem)
}