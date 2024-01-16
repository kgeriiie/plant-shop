package com.vml.tutorial.plantshop.core.data.account

import com.vml.tutorial.plantshop.MR.strings.login_error_text
import com.vml.tutorial.plantshop.core.domain.DataResult
import com.vml.tutorial.plantshop.core.presentation.UiText
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseUser

interface FirebaseAuthDataSource {
    suspend fun login(email: String, password: String): DataResult<FirebaseUser>
    suspend fun logout()

    suspend fun getCurrentUser(): FirebaseUser?
}

class FirebaseAuthDataSourceImpl(private val authService: FirebaseAuth): FirebaseAuthDataSource {
    override suspend fun login(email: String, password: String): DataResult<FirebaseUser> {
        return authService.signInWithEmailAndPassword(email, password)
            .user
            ?.let { DataResult.Success(it) }
            ?: DataResult.Failed(message = UiText.StringRes(login_error_text))
    }

    override suspend fun logout() {
        authService.signOut()
    }

    override suspend fun getCurrentUser(): FirebaseUser? {
        return authService.currentUser
    }
}
