buildscript {
    dependencies {
        classpath("com.google.firebase:firebase-appdistribution-gradle:4.0.1")
    }
}
plugins {
    alias(libs.plugins.jetbrainsCompose).apply(false)
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.kotlinSerialization).apply(false)
    alias(libs.plugins.sqlDelight).apply(false)
    alias(libs.plugins.mokoResourcesMultiplatform).apply(false)
    alias(libs.plugins.googleServices).apply(false)
    alias(libs.plugins.firebaseAppDistribution).apply(false)
    alias(libs.plugins.ksp).apply(false)
}
