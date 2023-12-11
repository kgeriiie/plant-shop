package com.vml.tutorial.plantshop.plants.presentation.favourites

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.Plant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavouritesComponent(
    componentContext: ComponentContext,
    private val plantsRepository: PlantsRepository,
    private val onNavigateToDetail: (plant: Plant) -> Unit
) : ComponentContext by componentContext {
    private val favorites: List<Plant> = plantsRepository.getFavorites()

    init {
        favorites.forEach { it.isFavorite = true }
    }

    private val _state = MutableStateFlow(FavoritesScreenState(favorites))
    val state: StateFlow<FavoritesScreenState> = _state.asStateFlow()

    fun onEvent(event: FavoritesScreenEvent) {
        when (event) {
            is FavoritesScreenEvent.OnFavoriteButtonClicked -> plantsRepository.toggleFavoriteStatus(
                event.item
            )

            is FavoritesScreenEvent.OnItemClicked -> onNavigateToDetail(event.item)
        }
    }
}
