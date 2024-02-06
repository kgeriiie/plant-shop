package com.vml.tutorial.plantshop.profile.orders.data

import com.vml.tutorial.plantshop.core.utils.exts.getCollection
import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem
import com.vml.tutorial.plantshop.profile.orders.domain.OrdersDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.Query
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.orderBy
import dev.gitlive.firebase.firestore.where

class FirebaseOrdersDataSource(
    private val storage: FirebaseFirestore = Firebase.firestore,
    private val ordersCollection: CollectionReference = storage.collection(ORDERS_COLLECTION)
): OrdersDataSource {
    override suspend fun fetchOrders(userId: String, queries: Map<String, String>): List<OrderItem> {
        return ordersCollection
            .where(Fields.USER_ID, userId)
            .where(Fields.STATUS, queries[QueryParam.STATUS])
            .orderByAsc(queries[QueryParam.ORDER_BY_ASC])
            .orderByDesc(queries[QueryParam.ORDER_BY_DESC])
            .limit(queries[QueryParam.LIMIT]?.toIntOrNull())
            .getCollection<OrderItem>()
    }

    private fun Query.orderByAsc(fieldName: String?): Query {
        return fieldName?.let { name ->
            return orderBy(name, direction = Direction.ASCENDING)
        }?: this
    }

    private fun Query.orderByDesc(fieldName: String?): Query {
        return fieldName?.let { name ->
            return orderBy(name, direction = Direction.DESCENDING)
        }?: this
    }

    private fun Query.limit(number: Int?): Query {
        return number?.let(::limit)?: this
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