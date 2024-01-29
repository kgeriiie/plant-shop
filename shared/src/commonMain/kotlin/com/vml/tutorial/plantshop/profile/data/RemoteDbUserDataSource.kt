package com.vml.tutorial.plantshop.profile.data

import com.vml.tutorial.plantshop.profile.domain.User
import com.vml.tutorial.plantshop.profile.domain.UserDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where

class RemoteDbUserDataSource : UserDataSource {
    private val collection = Firebase.firestore
    override suspend fun insertToDatabase(user: User) {
        collection.collection(COLLECTION_ID).add(user)
    }

    override suspend fun removeFromDatabase() {
        collection.collection(COLLECTION_ID).get().documents.map { document ->
            collection.document(document.id).delete()
        }
    }

    override suspend fun getUser(email: String?): User? {
        if (email.isNullOrEmpty()) return null

        val users: List<User>? = try {
            collection.collectionGroup(COLLECTION_ID).where(EMAIL_FIELD_NAME, email)
                .get().documents.map { it.data() }
        } catch (err: Exception) {
            err.printStackTrace()
            return null
        }
        return users?.firstOrNull()
    }

    companion object {
        const val COLLECTION_ID = "users"
        const val EMAIL_FIELD_NAME = "email"
    }
}