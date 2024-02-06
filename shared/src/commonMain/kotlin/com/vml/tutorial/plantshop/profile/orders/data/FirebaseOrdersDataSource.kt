package com.vml.tutorial.plantshop.profile.orders.data

import com.vml.tutorial.plantshop.core.utils.exts.getCollection
import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem
import com.vml.tutorial.plantshop.profile.orders.domain.OrdersDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.orderBy
import dev.gitlive.firebase.firestore.where

class FirebaseOrdersDataSource(
    private val storage: FirebaseFirestore = Firebase.firestore,
    private val ordersCollection: CollectionReference = storage.collection(ORDERS_COLLECTION)
): OrdersDataSource {
    override suspend fun fetchOrders(userId: String, queries: Map<String, String>): List<OrderItem> {
        var query = ordersCollection.where(Fields.USER_ID, userId)

        queries[QueryParam.STATUS]?.let { status ->
            query = query.where(Fields.STATUS, status)
        }

        queries[QueryParam.ORDER_BY_ASC]?.let { orderBy ->
            query = query.orderBy(orderBy, direction = Direction.ASCENDING)
        }

        queries[QueryParam.ORDER_BY_DESC]?.let { orderBy ->
            query = query.orderBy(orderBy, direction = Direction.DESCENDING)
        }

        queries[QueryParam.LIMIT]?.toIntOrNull()?.let { limit ->
            query = query.limit(limit)
        }

        return query.getCollection<OrderItem>()
    }

    override suspend fun updateOrder(order: OrderItem) {
        ordersCollection
            .document(order.id)
            .update(order)
    }

    override suspend fun insertOrder(order: OrderItem) {
        ordersCollection.add(order).also {
            updateOrder(order.copy(id = it.id))
        }
    }

    companion object {
        const val ORDERS_COLLECTION = "order-history"
        object Fields {
            const val USER_ID = "userId"
            const val STATUS = "status"
            const val UPDATE = "updatedAt"
        }

        object QueryParam {
            const val ORDER_BY_ASC = "ORDER_BY_ASC"
            const val ORDER_BY_DESC = "ORDER_BY_DESC"
            const val LIMIT = "LIMIT"
            const val STATUS = "STATUS"
        }
    }
}