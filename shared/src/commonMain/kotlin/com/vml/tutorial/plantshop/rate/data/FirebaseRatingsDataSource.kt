package com.vml.tutorial.plantshop.rate.data

import com.vml.tutorial.plantshop.core.utils.exts.getCollection
import com.vml.tutorial.plantshop.profile.orders.domain.OrderItem
import com.vml.tutorial.plantshop.rate.domain.Rating
import com.vml.tutorial.plantshop.rate.domain.RatingsDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where
import kotlinx.datetime.Clock

class FirebaseRatingsDataSource(
    private val storage: FirebaseFirestore = Firebase.firestore,
    private val ratingsCollection: CollectionReference = storage.collection(RATINGS)
): RatingsDataSource {
    override suspend fun getOrderRating(userId: String, orderId: String): Rating? {
        return ratingsCollection
            .where(USER_ID, userId)
            .where(ORDER_ID, orderId)
            .getCollection<Rating>().firstOrNull()
    }

    override suspend fun updateRating(rating: Rating) {
        ratingsCollection
            .document(rating.cId)
            .update(rating.copy(timestamp = Clock.System.now().epochSeconds))
    }

    override suspend fun insertRating(rating: Rating) {
        ratingsCollection.add(rating).also {
            updateRating(rating.copy(cId = it.id, timestamp = Clock.System.now().epochSeconds))
        }
    }

    companion object {
        private const val RATINGS = "ratings"
        private const val USER_ID = "userId"
        private const val ORDER_ID = "orderId"
    }
}