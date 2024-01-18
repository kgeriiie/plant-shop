package com.vml.tutorial.plantshop.core.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect class DataStoreUtil {
    fun dataStore(): DataStore<Preferences>
}