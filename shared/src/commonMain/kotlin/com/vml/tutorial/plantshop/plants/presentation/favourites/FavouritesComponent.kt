package com.vml.tutorial.plantshop.plants.presentation.favourites

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.Plant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet

class FavouritesComponent(
    componentContext: ComponentContext,
    private val plantsRepository: PlantsRepository,
    private val onNavigateToDetail: (plant: Plant) -> Unit
) : ComponentContext by componentContext {
    private val favouritesFlow: MutableStateFlow<List<Plant>> = MutableStateFlow(plantsRepository.getFavorites().map { it.copy(isFavorite = true) })

    private val _state = MutableStateFlow(FavoritesScreenState(listOf()))
    val state: StateFlow<FavoritesScreenState> = _state.combine(favouritesFlow) { state, favourites ->
        state.copy(favoritePlants = favourites)
    }.stateIn(componentContext.componentCoroutineScope(), SharingStarted.Lazily, _state.value)

    fun onEvent(event: FavoritesScreenEvent) {
        when (event) {
            is FavoritesScreenEvent.OnFavoriteButtonClicked -> {
                favouritesFlow.updateAndGet { plants ->
                    plants.filter { it.id != event.item.id }
                }

                plantsRepository.toggleFavoriteStatus(event.item)
            }

            is FavoritesScreenEvent.OnItemClicked -> onNavigateToDetail(event.item)
        }
    }
}
