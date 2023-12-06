package com.vml.tutorial.plantshop.core.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.vml.tutorial.plantshop.PlantDatabase

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver {
        return NativeSqliteDriver(PlantDatabase.Schema, "plant.db")
    }
}
