package com.vml.tutorial.plantshop.profile.data

import com.vml.tutorial.plantshop.profile.domain.User
import com.vml.tutorial.plantshop.profile.domain.UserDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class RemoteDbUserDataSource : UserDataSource {
    private val collection = Firebase.firestore.collection("user")
    override suspend fun insertToDatabase(user: User) {
        collection.add(user)
    }

    override suspend fun removeFromDatabase() {
        collection.get().documents.map { document ->
            collection.document(document.id).delete()
        }
    }

    override suspend fun getUser(): User? {
        val users: List<User>? = try {
            collection.get().documents.map { it.data() }
        } catch (err: Exception) {
            null
        }
        return users?.firstOrNull()
    }

    override suspend fun isThereUser(): Boolean {
        return getUser() != null
    }
}