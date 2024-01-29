package com.vml.tutorial.plantshop.profile.data

import com.vml.tutorial.plantshop.profile.domain.User
import com.vml.tutorial.plantshop.profile.domain.UserDataSource
import com.vml.tutorial.plantshop.rate.domain.Rating
import com.vml.tutorial.plantshop.rate.domain.RatingsDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth

interface ProfileRepository {
    suspend fun insertToDatabase(user: User)
    suspend fun removeFromDatabase()
    suspend fun getUser(): User?
    suspend fun saveRating(rating: Rating)
    suspend fun getOrderRating(orderId: String?): Rating?
}

class ProfileRepositoryImpl(
    private val dbUserDataSource: UserDataSource,
    private val remoteDbUserDataSource: UserDataSource,
    private val ratingsDataSource: RatingsDataSource
) : ProfileRepository {
    override suspend fun insertToDatabase(user: User) {
        dbUserDataSource.insertToDatabase(user)
    }

    override suspend fun removeFromDatabase() {
        dbUserDataSource.removeFromDatabase()
    }

    override suspend fun getUser(): User? {
        if (!dbUserDataSource.isThereUser()) {
            Firebase.auth.currentUser?.email?.let { email ->
                remoteDbUserDataSource.getUser(email)?.let { dbUserDataSource.insertToDatabase(it) }
            }
        }
        return dbUserDataSource.getUser()
    }

    override suspend fun saveRating(rating: Rating) {
        val userId = Firebase.auth.currentUser?.uid?: return
        getOrderRating(rating.orderId)?.let { previous ->
            ratingsDataSource.updateRating(rating.copy(cId = previous.cId))
        }?: ratingsDataSource.insertRating(rating.copy(userId = userId))
    }

    override suspend fun getOrderRating(orderId: String?): Rating? {
        val orderId = orderId?: return null
        val userId = Firebase.auth.currentUser?.uid?: return null
        return ratingsDataSource.getOrderRating(userId, orderId)
    }
}
