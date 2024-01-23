package com.vml.tutorial.plantshop.register.data

import com.vml.tutorial.plantshop.profile.domain.User
import com.vml.tutorial.plantshop.profile.domain.UserDataSource

interface RegisterUserRepository {
    suspend fun insertToDatabase(user: User)
}

class RegisterUserRepositoryImpl(
    private val remoteDbUserDataSource: UserDataSource
): RegisterUserRepository {
    override suspend fun insertToDatabase(user: User) {
        remoteDbUserDataSource.insertToDatabase(user)
    }
}
