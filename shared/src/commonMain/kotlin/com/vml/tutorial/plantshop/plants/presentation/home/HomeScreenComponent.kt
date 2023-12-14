package com.vml.tutorial.plantshop.plants.presentation.home

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.PlantType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeScreenComponent(
    componentContext: ComponentContext,
    private val plantsRepository: PlantsRepository,
    private val onNavigateToDetail: (plant: Plant) -> Unit
) : ComponentContext by componentContext {
    private val plantsFlow: Flow<List<Plant>> =
        flow { emitAll(plantsRepository.getPlants()) }

    private var favoritePlants: Flow<List<Plant>> =
        flow { emitAll(plantsRepository.getFavorites()) }

    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> =
        combine(_state, plantsFlow, favoritePlants) { state, plants, favorites ->
            state.copy(plants = plants, favoritePlants = favorites)
        }.stateIn(
            componentContext.componentCoroutineScope(),
            SharingStarted.WhileSubscribed(),
            HomeScreenState()
        )

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnItemClicked -> onNavigateToDetail(event.item)
            is HomeScreenEvent.OnFavoriteButtonClicked -> {
                componentCoroutineScope().launch {
                    plantsRepository.toggleFavoriteStatus(event.item.id)
                }
            }

            HomeScreenEvent.OnOfferClicked -> Unit //TODO()
            HomeScreenEvent.OnProfileClicked -> Unit //TODO()
            is HomeScreenEvent.OnSearchClicked -> Unit //TODO()
            is HomeScreenEvent.OnCategoryClicked -> {
                onCategoryClicked(event.plantType)
            }
        }
    }

    private fun onCategoryClicked(plantType: PlantType) {
        when (plantType) {
            PlantType.GREEN -> Unit //TODO()
            PlantType.FLOWER -> Unit //TODO()
            PlantType.INDOOR -> Unit //TODO()
        }
    }
}
