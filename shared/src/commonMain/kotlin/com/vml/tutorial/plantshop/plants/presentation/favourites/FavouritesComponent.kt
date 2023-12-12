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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class FavouritesComponent(
    componentContext: ComponentContext,
    private val plantsRepository: PlantsRepository,
    private val onNavigateToDetail: (plant: Plant) -> Unit
) : ComponentContext by componentContext {
    //private val favouritesFlow: MutableStateFlow<List<Plant>> =
        //MutableStateFlow(plantsRepository.getFavorites().map { it.copy(isFavorite = true) })

    //private val _state = MutableStateFlow(FavoritesScreenState(listOf()))
    //val state: StateFlow<FavoritesScreenState> =
    //    _state.combine(favouritesFlow) { state, favourites ->
    //        state.copy(favoritePlants = favourites)
    //    }.stateIn(componentContext.componentCoroutineScope(), SharingStarted.Lazily, _state.value)

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
                //favouritesFlow.updateAndGet { plants ->   //TODO: Reconsider when merging
                //    plants.filter { it.id != event.item.id }
                //}

                //plantsRepository.toggleFavoriteStatus(event.item)
                plantsRepository.toggleFavoriteStatus(
                    event.item.id
                )
            }

            is FavoritesScreenEvent.OnItemClicked -> onNavigateToDetail(event.item)
        }
    }
}

object FavouritesComponentConstants {
    const val STOP_TIMEOUT_MILLIS = 5000L
}
