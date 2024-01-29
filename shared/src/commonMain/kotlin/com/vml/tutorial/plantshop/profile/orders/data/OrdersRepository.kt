package com.vml.tutorial.plantshop.profile.orders.data

import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem

interface OrdersRepository {
    suspend fun getOrders(): List<OrderItem>
}

class OrdersRepositoryImpl(private val remoteDataSource: FirebaseOrdersDataSource): OrdersRepository {
    override suspend fun getOrders(): List<OrderItem> = remoteDataSource.fetchOrders()
}