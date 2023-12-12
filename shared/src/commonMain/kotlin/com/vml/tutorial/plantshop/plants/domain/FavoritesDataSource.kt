package com.vml.tutorial.plantshop.plants.domain

import kotlinx.coroutines.flow.Flow

interface FavoritesDataSource {
    fun getFavorites(favoriteIds: Flow<List<Long>>, plants: List<Plant>): Flow<List<Plant>>
}