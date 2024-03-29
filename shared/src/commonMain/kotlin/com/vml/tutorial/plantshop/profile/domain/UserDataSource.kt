package com.vml.tutorial.plantshop.profile.domain

interface UserDataSource {
    suspend fun insertToDatabase(user: User)
    suspend fun removeFromDatabase()
    suspend fun getUser(email: String?): User?
    suspend fun isThereUser(): Boolean = true
}