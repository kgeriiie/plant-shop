package com.vml.tutorial.plantshop.plants.presentation.home

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.PlantCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenComponent(
    componentContext: ComponentContext,
    private val plantsRepository: PlantsRepository,
    private val onNavigateToDetail: (plant: Plant) -> Unit
) : ComponentContext by componentContext {
    private val allPlants by lazy { plantsRepository.getPlants(PlantCategory.NONE) }
    private val plantsFlow: MutableStateFlow<List<Plant>> = MutableStateFlow(listOf())
    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> =
        combine(_state, plantsFlow, plantsRepository.getFavorites()) { state, plants, favorites ->
            state.copy(
                plants = plants.map { plant -> plant.copy(isFavorite = favorites.any { it.id == plant.id}) }
            )
        }.stateIn(
            componentContext.componentCoroutineScope(),
            SharingStarted.WhileSubscribed(),
            HomeScreenState()
        )

    init {
        plantsFlow.tryEmit(plantsRepository.getPlants(PlantCategory.GREEN))
        _state.update { it.copy(chosenCategory = PlantCategory.GREEN) }
    }

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
            is HomeScreenEvent.OnSearchQueryChanged -> {
                if (event.query.isNotBlank()) {
                    _state.update {
                        it.copy(searchResults = filterPlantsByName(event.query))
                    }
                }
            }

            is HomeScreenEvent.OnCategoryClicked -> {
                plantsFlow.update { plantsRepository.getPlants(event.plantCategory) }
                _state.update { it.copy(chosenCategory = event.plantCategory) }
            }

            is HomeScreenEvent.OnResultItemClicked -> {
                onNavigateToDetail(event.item)
                _state.update {
                    it.copy(searchResults = emptyList())
                }
            }
        }
    }

    private fun filterPlantsByName(query: String): List<Plant> {
        return allPlants.filter { plant ->
            plant.name.contains(query, ignoreCase = true)
        }
    }
}
