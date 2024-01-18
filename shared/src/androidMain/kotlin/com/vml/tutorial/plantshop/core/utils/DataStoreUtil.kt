package com.vml.tutorial.plantshop.core.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.vml.tutorial.plantshop.core.data.AppDataStore
import okio.Path.Companion.toPath

actual class DataStoreUtil(private val context: Context) {
    actual fun dataStore(): DataStore<Preferences> {
        return PreferenceDataStoreFactory.createWithPath(
            corruptionHandler = null,
            migrations = emptyList(),
            produceFile = { context.filesDir.resolve(AppDataStore.storeName).absolutePath.toPath() }
        )
    }
}