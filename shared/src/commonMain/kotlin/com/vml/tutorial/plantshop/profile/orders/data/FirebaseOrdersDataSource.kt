package com.vml.tutorial.plantshop.profile.orders.data

import com.vml.tutorial.plantshop.core.utils.exts.getCollection
import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem
import com.vml.tutorial.plantshop.profile.orders.domain.OrdersDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where

class FirebaseOrdersDataSource(
    private val storage: FirebaseFirestore = Firebase.firestore,
    private val ordersCollection: CollectionReference = storage.collection(ORDERS)
): OrdersDataSource {
    override suspend fun fetchOrders(userId: String): List<OrderItem> {
        return ordersCollection.where(USER_ID, userId).getCollection<OrderItem>()
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
        const val ORDERS = "order-history"
        const val USER_ID = "userId"
    }
}