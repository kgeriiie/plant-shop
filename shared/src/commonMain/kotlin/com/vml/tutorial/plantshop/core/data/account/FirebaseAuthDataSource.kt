package com.vml.tutorial.plantshop.core.data.account

import com.vml.tutorial.plantshop.MR.strings.login_error_text
import com.vml.tutorial.plantshop.core.domain.DataResult
import com.vml.tutorial.plantshop.core.presentation.UiText
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.FirebaseAuthInvalidCredentialsException
import dev.gitlive.firebase.auth.FirebaseUser

interface FirebaseAuthDataSource {
    suspend fun login(email: String, password: String): DataResult<FirebaseUser>
    suspend fun logout()
    suspend fun getCurrentUser(): FirebaseUser?
    suspend fun deleteUser()
}

class FirebaseAuthDataSourceImpl(private val authService: FirebaseAuth): FirebaseAuthDataSource {
    override suspend fun login(email: String, password: String): DataResult<FirebaseUser> {
        return try {
            return authService.signInWithEmailAndPassword(email, password)
                .user
                ?.let { DataResult.Success(it) }
                ?: DataResult.Failed(message = UiText.StringRes(login_error_text))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            DataResult.Failed(message = UiText.DynamicString(e.message.orEmpty()))
        } catch (e: Exception) {
            DataResult.Failed(message = UiText.DynamicString(e.message.orEmpty()))
        }
    }

    override suspend fun logout() {
        authService.signOut()
    }

    override suspend fun getCurrentUser(): FirebaseUser? {
        return authService.currentUser
    }

    override suspend fun deleteUser() {
        authService.currentUser?.delete()
    }
}
