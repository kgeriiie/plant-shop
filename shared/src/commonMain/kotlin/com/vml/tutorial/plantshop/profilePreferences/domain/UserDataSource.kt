package com.vml.tutorial.plantshop.profilePreferences.domain

interface UserDataSource {
    suspend fun insertToDatabase(user: User)
    suspend fun removeFromDatabase()
    suspend fun getUser(email: String?): User?
    suspend fun isThereUser(): Boolean = true
    suspend fun updateUserInfo(user: User)
}