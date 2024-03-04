package com.vml.tutorial.plantshop.profilePreferences.data

import com.vml.tutorial.plantshop.profilePreferences.domain.User
import com.vml.tutorial.plantshop.profilePreferences.domain.UserDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

interface ProfileRepository {
    suspend fun insertToDatabase(user: User)
    suspend fun removeFromDatabase()
    suspend fun getUser(): User?
    suspend fun updateUserInfo(user: User): Boolean
    suspend fun deleteUser(cId: String)
    suspend fun logout()
}

class ProfileRepositoryImpl(
    private val dbUserDataSource: UserDataSource, private val remoteDbUserDataSource: UserDataSource
) : ProfileRepository {
    override suspend fun insertToDatabase(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            remoteDbUserDataSource.insertToDatabase(user)
            dbUserDataSource.insertToDatabase(user)
        }.join()
    }

    override suspend fun removeFromDatabase() {
        dbUserDataSource.removeFromDatabase(null)
    }

    override suspend fun getUser(): User? {
        CoroutineScope(Dispatchers.IO).launch {
            if (!dbUserDataSource.isThereUser()) {
                Firebase.auth.currentUser?.email?.let { email ->
                    remoteDbUserDataSource.getUser(email)?.let {
                        dbUserDataSource.insertToDatabase(it)
                    }
                }
            }
        }.join()
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

    override suspend fun deleteUser(cId: String) {
        dbUserDataSource.removeFromDatabase(null)
        remoteDbUserDataSource.removeFromDatabase(cId)
    }

    override suspend fun logout() {
        dbUserDataSource.removeFromDatabase(null)
    }
}
