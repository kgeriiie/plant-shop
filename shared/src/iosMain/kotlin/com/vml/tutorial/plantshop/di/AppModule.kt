package com.vml.tutorial.plantshop.di

import com.vml.tutorial.plantshop.PlantDatabase
import com.vml.tutorial.plantshop.basket.data.BasketRepository
import com.vml.tutorial.plantshop.basket.data.BasketRepositoryImpl
import com.vml.tutorial.plantshop.basket.data.DbBasketItemsDataSource
import com.vml.tutorial.plantshop.core.data.AppDataStore
import com.vml.tutorial.plantshop.core.data.AppDataStoreImpl
import com.vml.tutorial.plantshop.core.data.DatabaseDriverFactory
import com.vml.tutorial.plantshop.core.data.account.AuthRepository
import com.vml.tutorial.plantshop.core.data.account.AuthRepositoryImpl
import com.vml.tutorial.plantshop.core.data.account.FirebaseAuthDataSource
import com.vml.tutorial.plantshop.core.data.account.FirebaseAuthDataSourceImpl
import com.vml.tutorial.plantshop.core.utils.DataStoreUtil
import com.vml.tutorial.plantshop.core.utils.ShareHelper
import com.vml.tutorial.plantshop.core.utils.ShareHelperImpl
import com.vml.tutorial.plantshop.core.utils.ShareUtils
import com.vml.tutorial.plantshop.plants.data.DbFavoritesDataSource
import com.vml.tutorial.plantshop.plants.data.DbPlantsDataSource
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.data.PlantsRepositoryImpl
import com.vml.tutorial.plantshop.plants.data.RemoteDbPlantsDataSource
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource
import com.vml.tutorial.plantshop.profilePreferences.data.DbUserDataSource
import com.vml.tutorial.plantshop.profilePreferences.data.ProfileRepository
import com.vml.tutorial.plantshop.profilePreferences.data.ProfileRepositoryImpl
import com.vml.tutorial.plantshop.profilePreferences.data.RemoteDbUserDataSource
import com.vml.tutorial.plantshop.profilePreferences.domain.UserDataSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth

actual class AppModule {
    private val db: PlantDatabase by lazy {
        PlantDatabase(
            driver = DatabaseDriverFactory().create()
        )
    }

    private val firebaseAuthDataSource: FirebaseAuthDataSource by lazy {
        FirebaseAuthDataSourceImpl(
            Firebase.auth
        )
    }

    actual val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(
            firebaseAuthDataSource
        )
    }

    actual val dbFavoritesDataSource: DbFavoritesDataSource by lazy {
        DbFavoritesDataSource(db)
    }

    actual val remoteDbPlantsDataSource: PlantsDataSource by lazy {
        RemoteDbPlantsDataSource()
    }

    actual val dbPlantsDataSource: PlantsDataSource by lazy {
        DbPlantsDataSource(db)
    }

    actual val plantsRepository: PlantsRepository by lazy {
        PlantsRepositoryImpl(
            dbPlantsDataSource,
            remoteDbPlantsDataSource,
            dbFavoritesDataSource
        )
    }

    actual val basketRepository: BasketRepository by lazy {
        BasketRepositoryImpl(
            DbBasketItemsDataSource(db)
        )
    }

    actual val shareHelper: ShareHelper by lazy {
        ShareHelperImpl()
    }

    actual val dataStore: AppDataStore by lazy {
        AppDataStoreImpl(DataStoreUtil().dataStore())
    }

    actual val dbUserDataSource: UserDataSource by lazy {
        DbUserDataSource(db)
    }

    actual val remoteDbUserDataSource: UserDataSource by lazy {
        RemoteDbUserDataSource()
    }

    actual val profileRepository: ProfileRepository by lazy {
        ProfileRepositoryImpl(
            dbUserDataSource = dbUserDataSource,
            remoteDbUserDataSource = remoteDbUserDataSource
        )
    }
}
