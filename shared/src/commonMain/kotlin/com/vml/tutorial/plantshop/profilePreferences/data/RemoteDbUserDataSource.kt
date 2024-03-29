package com.vml.tutorial.plantshop.profilePreferences.data

import com.vml.tutorial.plantshop.profilePreferences.domain.User
import com.vml.tutorial.plantshop.profilePreferences.domain.UserDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where

class RemoteDbUserDataSource : UserDataSource {
    private val collection = Firebase.firestore
    override suspend fun insertToDatabase(user: User) {
        val newDocument = collection.collection(COLLECTION_ID).document
        user.cId = newDocument.id
        newDocument.set(user)
    }

    override suspend fun removeFromDatabase(cId: String?) {
        cId?.let { collection.collection(COLLECTION_ID).document(it).delete() }
    }

    override suspend fun getUser(email: String?): User? {
        val users: List<User>? = try {
            collection.collectionGroup(COLLECTION_ID).where(EMAIL_FIELD_NAME, email)
                .get().documents.map { it.data() }
        } catch (err: Exception) {
            return null
        }
        return users?.firstOrNull()
    }

    override suspend fun updateUserInfo(user: User) {
        user.cId?.let { collection.collection(COLLECTION_ID).document(it).set(user) }
    }

    companion object {
        const val COLLECTION_ID = "users"
        const val EMAIL_FIELD_NAME = "email"
    }
}
