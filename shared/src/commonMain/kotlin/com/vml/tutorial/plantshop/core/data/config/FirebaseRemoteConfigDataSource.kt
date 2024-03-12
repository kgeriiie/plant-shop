package com.vml.tutorial.plantshop.core.data.config

import dev.gitlive.firebase.remoteconfig.FirebaseRemoteConfig

interface FirebaseRemoteConfigDataSource {
    suspend fun getConfig(key: String): String?
}

class FirebaseRemoteConfigDataSourceImpl(private val firebaseRemoteConfig: FirebaseRemoteConfig) :
    FirebaseRemoteConfigDataSource {
    override suspend fun getConfig(key: String): String? {
        return try {
            firebaseRemoteConfig.fetchAndActivate()
            firebaseRemoteConfig.getValue(key).asString()

            firebaseRemoteConfig.
        } catch (err: Exception) {
            null
        }
    }
}
