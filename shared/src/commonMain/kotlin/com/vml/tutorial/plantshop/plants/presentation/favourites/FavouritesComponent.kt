package com.vml.tutorial.plantshop.plants.presentation.favourites

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.favourites.FavouritesComponentConstants.STOP_TIMEOUT_MILLIS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavouritesComponent(
    componentContext: ComponentContext,
    private val plantsRepository: PlantsRepository,
    private val onNavigateToDetail: (plant: Plant) -> Unit
) : ComponentContext by componentContext {
    private val _state = MutableStateFlow(FavoritesScreenState())
    val state = combine(_state, plantsRepository.getFavorites()) { state, plants ->
        state.copy(favoritePlants = plants)
    }.stateIn(
        CoroutineScope(Dispatchers.Main),
        SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        FavoritesScreenState()
    )

    fun onEvent(event: FavoritesScreenEvent) {
        when (event) {
            is FavoritesScreenEvent.OnFavoriteButtonClicked -> {
                componentCoroutineScope().launch {
                    plantsRepository.toggleFavoriteStatus(event.item.id)
                }
            }

            is FavoritesScreenEvent.OnItemClicked -> onNavigateToDetail(event.item)
        }
    }
}

object FavouritesComponentConstants {
    const val STOP_TIMEOUT_MILLIS = 5000L
}
