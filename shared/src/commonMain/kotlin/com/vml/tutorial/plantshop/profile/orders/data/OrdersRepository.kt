package com.vml.tutorial.plantshop.profile.orders.data

import com.vml.tutorial.plantshop.core.data.account.FirebaseAuthDataSource
import com.vml.tutorial.plantshop.core.utils.Logger
import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem
import com.vml.tutorial.plantshop.profile.orders.domain.OrderStatus
import com.vml.tutorial.plantshop.profile.orders.data.FirebaseOrdersDataSource.Companion.QueryParam
import com.vml.tutorial.plantshop.profile.orders.data.FirebaseOrdersDataSource.Companion.Fields
import kotlinx.datetime.Clock

interface OrdersRepository {
    suspend fun getOrders(status: OrderStatus? = null, limit: Int? = null): List<OrderItem>
    suspend fun cancelOrder(orderId: String): Boolean

    suspend fun createAnOrder(itemIds: List<Int>, totalPrice: Double, currency: String): Boolean
}

class OrdersRepositoryImpl(
    private val authDataSource: FirebaseAuthDataSource,
    private val remoteDataSource: FirebaseOrdersDataSource
): OrdersRepository {
    override suspend fun getOrders(status: OrderStatus?, limit: Int?): List<OrderItem> {
        val user = authDataSource.getCurrentUser()?: return emptyList()
        val queryParams = buildMap {
            status?.let {
                put(QueryParam.STATUS, it.name.lowercase())
            }
            limit?.let { limit ->
                put(QueryParam.LIMIT, limit.toString())
                put(QueryParam.ORDER_BY_DESC, Fields.UPDATE)
            }
        }
        return remoteDataSource.fetchOrders(user.uid, queryParams)
    }

    override suspend fun cancelOrder(orderId: String): Boolean {
        val ord = getOrders()
        Logger.d("test--","orders: ${ord.size}")
        return getOrders().firstOrNull { it.id == orderId }?.let {orderItem ->
            remoteDataSource.updateOrder(orderItem.copy(
                status = OrderStatus.CANCELLED,
                updatedAt = Clock.System.now().epochSeconds
            ))
            true
        }?: false
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