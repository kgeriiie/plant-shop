package com.vml.tutorial.plantshop.di

import com.vml.tutorial.plantshop.basket.data.BasketRepository
import com.vml.tutorial.plantshop.core.data.AppDataStore
import com.vml.tutorial.plantshop.core.data.account.AuthRepository
import com.vml.tutorial.plantshop.core.utils.ShareUtils
import com.vml.tutorial.plantshop.plants.data.DbFavoritesDataSource
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource
import com.vml.tutorial.plantshop.profile.data.ProfileRepository
import com.vml.tutorial.plantshop.profile.domain.UserDataSource
import com.vml.tutorial.plantshop.register.data.RegisterUserRepository

expect class AppModule {
    val authRepository: AuthRepository
    val dbPlantsDataSource: PlantsDataSource
    val remoteDbPlantsDataSource: PlantsDataSource
    val plantsRepository: PlantsRepository
    val dbFavoritesDataSource: DbFavoritesDataSource
    val basketRepository: BasketRepository
    val shareUtils: ShareUtils
    val dataStore: AppDataStore
    val profileRepository: ProfileRepository
    val dbUserDataSource: UserDataSource
    val remoteDbUserDataSource: UserDataSource
    val registerUserRepository: RegisterUserRepository
}
