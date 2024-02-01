package com.vml.tutorial.plantshop.profile.orders.data

import com.vml.tutorial.plantshop.core.data.account.FirebaseAuthDataSource
import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem

interface OrdersRepository {
    suspend fun getOrders(): List<OrderItem>
}

class OrdersRepositoryImpl(
    private val authDataSource: FirebaseAuthDataSource,
    private val remoteDataSource: FirebaseOrdersDataSource
): OrdersRepository {
    override suspend fun getOrders(): List<OrderItem> {
        val user = authDataSource.getCurrentUser()?: return emptyList()
        return remoteDataSource.fetchOrders(user.uid)
    }
}