package com.vml.tutorial.plantshop.core.data.config

interface ConfigRepository {
    suspend fun getConfig(key: String): String?
}

class ConfigRepositoryImpl(private val firebaseRemoteConfigDataSource: FirebaseRemoteConfigDataSource) :
    ConfigRepository {
    override suspend fun getConfig(key: String): String? {
        return firebaseRemoteConfigDataSource.getConfig(key)
    }
}
