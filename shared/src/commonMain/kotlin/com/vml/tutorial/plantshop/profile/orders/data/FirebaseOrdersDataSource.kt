package com.vml.tutorial.plantshop.profile.orders.data

import com.vml.tutorial.plantshop.core.utils.exts.getCollection
import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem
import com.vml.tutorial.plantshop.profile.orders.domain.OrdersDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore

class FirebaseOrdersDataSource(
    private val storage: FirebaseFirestore = Firebase.firestore,
    private val ordersCollection: CollectionReference = storage.collection(ORDERS)
): OrdersDataSource {
    override suspend fun fetchOrders(): List<OrderItem> {
        return ordersCollection.getCollection()
    }

    override suspend fun insertOrder(order: OrderItem) {
        TODO("Not yet implemented")
    }

    companion object {
        const val ORDERS = "order-history"
    }
}