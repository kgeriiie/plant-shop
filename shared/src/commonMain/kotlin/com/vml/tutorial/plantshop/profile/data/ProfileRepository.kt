package com.vml.tutorial.plantshop.profile.data

import com.vml.tutorial.plantshop.profile.domain.User
import com.vml.tutorial.plantshop.profile.domain.UserDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth

interface ProfileRepository {
    suspend fun insertToDatabase(user: User)
    suspend fun removeFromDatabase()
    suspend fun getUser(): User?
}

class ProfileRepositoryImpl(
    private val dbUserDataSource: UserDataSource,
    private val remoteDbUserDataSource: UserDataSource
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
        return dbUserDataSource.getUser(null)
    }
}
