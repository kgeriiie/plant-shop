import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.mokoResourcesMultiplatform)
    alias(libs.plugins.googleServices)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    targets.withType(org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget::class.java).all {
        binaries.withType(org.jetbrains.kotlin.gradle.plugin.mpp.Framework::class.java).all {
            export(libs.mvvm.core)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
            export(libs.moko.resources.common)
            export(libs.moko.graphics)
        }
    }

    sourceSets {
        val androidMain by getting {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.appcompat)
                implementation(libs.sql.delight.android)
                implementation(libs.ktor.android)
                implementation(libs.decompose)
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(libs.sql.delight.coroutines)
                implementation(libs.kotlinx.date)
                implementation(libs.mvvm.core)
                implementation(libs.mvvm.compose)
                implementation(libs.mvvm.flow)
                implementation(libs.mvvm.flow.compose)

                implementation(libs.bundles.ktor)

                implementation(libs.kotlin.json.serialization)

                implementation(libs.moko.resources.common)
                implementation(libs.moko.resources.compose)

                implementation(libs.decompose)
                implementation(libs.decompose.jetbrains)

                // For async image loading
                implementation(libs.kamel.image)

                implementation(libs.firebase.firestore)
                implementation(libs.firebase.common)
                implementation(libs.firebase.auth)

                implementation(libs.datastore)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.sql.delight.native)
                implementation(libs.ktor.ios)
            }
        }
    }
}

android {
    namespace = "com.vml.tutorial.plantshop"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

sqldelight {
    databases {
        create(name = "PlantDatabase") {
            packageName.set("com.vml.tutorial.plantshop")
        }
    }
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    commonMainApi(libs.mvvm.core)
    commonMainApi(libs.mvvm.compose)
    commonMainApi(libs.mvvm.flow)
    commonMainApi(libs.mvvm.flow.compose)
    commonMainApi(libs.moko.resources.common)
    commonMainApi(libs.moko.resources.compose)
}

multiplatformResources {
    multiplatformResourcesPackage = "com.vml.tutorial.plantshop"
}
