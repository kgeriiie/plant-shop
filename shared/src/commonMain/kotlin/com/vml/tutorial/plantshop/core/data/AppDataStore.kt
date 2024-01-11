package com.vml.tutorial.plantshop.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.vml.tutorial.plantshop.core.utils.exts.orFalse
import kotlinx.coroutines.flow.firstOrNull

interface AppDataStore {
    suspend fun appLaunchedBefore(): Boolean
    suspend fun setAppLaunchedBefore(launched: Boolean)

    companion object {
        val storeName: String = "plantshop.preferences_pb"
    }
}

class AppDataStoreImpl(private val dataStore: DataStore<Preferences>): AppDataStore {

    companion object {
        private val appLaunchedBefore = booleanPreferencesKey("app_launched_before")
    }

    override suspend fun appLaunchedBefore(): Boolean {
        return dataStore.data.firstOrNull()?.get(appLaunchedBefore).orFalse()
    }

    override suspend fun setAppLaunchedBefore(launched: Boolean) {
        dataStore.edit { preferences -> preferences[appLaunchedBefore] = launched }
    }
}