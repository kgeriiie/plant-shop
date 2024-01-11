package com.vml.tutorial.plantshop.core.data.account

import com.vml.tutorial.plantshop.core.domain.DataResult
import dev.gitlive.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun login(email: String, password: String): DataResult<FirebaseUser>
    suspend fun logout()
    suspend fun isAuthenticated(): Boolean
}

class AuthRepositoryImpl(
    private val firebaseDataSource: FirebaseAuthDataSource
): AuthRepository {
    override suspend fun login(email: String, password: String): DataResult<FirebaseUser> {
        return firebaseDataSource.login(email, password)
    }

    override suspend fun logout() {
        return firebaseDataSource.logout()
    }

    override suspend fun isAuthenticated(): Boolean {
        return firebaseDataSource.getCurrentUser() != null
    }
}