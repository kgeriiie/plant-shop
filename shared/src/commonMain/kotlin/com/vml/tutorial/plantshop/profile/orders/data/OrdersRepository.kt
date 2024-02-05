package com.vml.tutorial.plantshop.profile.orders.data

import com.vml.tutorial.plantshop.core.data.account.FirebaseAuthDataSource
import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem
import com.vml.tutorial.plantshop.profile.orders.domain.OrderStatus
import kotlinx.datetime.Clock

interface OrdersRepository {
    suspend fun getOrders(): List<OrderItem>
    suspend fun cancelOrder(orderId: String)

    suspend fun createAnOrder(itemIds: List<Int>, totalPrice: Double, currency: String): Boolean
}

class OrdersRepositoryImpl(
    private val authDataSource: FirebaseAuthDataSource,
    private val remoteDataSource: FirebaseOrdersDataSource
): OrdersRepository {
    override suspend fun getOrders(): List<OrderItem> {
        val user = authDataSource.getCurrentUser()?: return emptyList()
        return remoteDataSource.fetchOrders(user.uid)
    }

    override suspend fun cancelOrder(orderId: String) {
        getOrders().firstOrNull { it.id == orderId }?.let {orderItem ->
            remoteDataSource.updateOrder(orderItem.copy(
                status = OrderStatus.CANCELLED,
                updatedAt = Clock.System.now().epochSeconds
            ))
        }
    }

    override suspend fun createAnOrder(itemIds: List<Int>, totalPrice: Double, currency: String): Boolean {
        val user = authDataSource.getCurrentUser()?: return false
        val order = OrderItem(
            id = "",
            orderNumber = generateOrderNumber(DEF_ORDER_NUM_LENGTH),
            plantIds = itemIds,
            status = OrderStatus.PENDING,
            currency = currency,
            totalPrice = totalPrice,
            createdAt = Clock.System.now().epochSeconds,
            updatedAt = Clock.System.now().epochSeconds,
            userId = user.uid
        )

        remoteDataSource.insertOrder(order)

        return true
    }

    companion object {
        private const val DEF_ORDER_NUM_LENGTH = 8
        private fun generateOrderNumber(length: Int): String {
            val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
        }
    }
}