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
    private var allPlants: List<Plant>? = null
    private val plantsFlow: MutableStateFlow<List<Plant>?> = MutableStateFlow(listOf())
    private val _state = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> =
        combine(_state, plantsFlow, plantsRepository.getFavorites()) { state, plants, favorites ->
            state.copy(
                plants = plants?.map { plant -> plant.copy(isFavorite = favorites.any { it.id == plant.id }) }
            )
        }.stateIn(
            componentContext.componentCoroutineScope(),
            SharingStarted.WhileSubscribed(),
            HomeScreenState()
        )

    init {
        componentCoroutineScope().launch {
            allPlants = plantsRepository.getPlants()
            plantsFlow.emit(filterPlants(plantCategory = PlantCategory.GREEN))
        }
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
                        it.copy(
                            searchResults = filterPlants(plantName = event.query) ?: emptyList()
                        )
                    }
                }
            }

            is HomeScreenEvent.OnCategoryClicked -> {
                plantsFlow.update { filterPlants(plantCategory = event.plantCategory) }
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

    private fun filterPlants(
        plantName: String? = null,
        plantCategory: PlantCategory? = null
    ): List<Plant>? {
        return allPlants?.filter { plant ->
            val nameMatches =
                plantName.isNullOrBlank() || plant.name.contains(plantName, ignoreCase = true)
            val categoryMatches = plantCategory == null || plant.types.contains(plantCategory.type)
            nameMatches && categoryMatches
        }
    }
}
