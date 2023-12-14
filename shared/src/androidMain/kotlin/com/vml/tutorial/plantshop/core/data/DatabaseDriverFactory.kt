package com.vml.tutorial.plantshop.core.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.vml.tutorial.plantshop.PlantDatabase

actual class DatabaseDriverFactory(
    private val context: Context
) {
    actual fun create(): SqlDriver {
        return AndroidSqliteDriver(PlantDatabase.Schema, context, "plant.db")
    }
}
