package com.vml.tutorial.plantshop.plants.presentation.favourites.components

import com.vml.tutorial.plantshop.plants.domain.Plant

sealed interface FavoritesScreenEvent {
    data class OnItemClicked(val item: Plant): FavoritesScreenEvent
    data class OnFavoriteButtonClicked(val item: Plant): FavoritesScreenEvent
}