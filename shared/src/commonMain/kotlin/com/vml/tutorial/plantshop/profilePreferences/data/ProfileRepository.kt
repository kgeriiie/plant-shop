package com.vml.tutorial.plantshop.profilePreferences.data

import com.vml.tutorial.plantshop.profilePreferences.domain.User
import com.vml.tutorial.plantshop.profilePreferences.domain.UserDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth

interface ProfileRepository {
    suspend fun insertToDatabase(user: User)
    suspend fun removeFromDatabase()
    suspend fun getUser(): User?
    suspend fun updateUserInfo(user: User): Boolean
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

    override suspend fun updateUserInfo(user: User): Boolean {
        try {
            remoteDbUserDataSource.updateUserInfo(user)
            dbUserDataSource.updateUserInfo(user)
        } catch (err: Exception) {
            return false
        }
        return true
    }
}
