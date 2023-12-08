package com.vml.tutorial.plantshop.plants.presentation.home

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.PlantType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenComponent(
    componentContext: ComponentContext,
    private val plantsRepository: PlantsRepository,
    private val onNavigateToDetail: (plant: Plant) -> Unit
) : ComponentContext by componentContext {

    private val _state = MutableStateFlow(HomeScreenState(plantsRepository.getPlants()))
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnItemClicked -> onNavigateToDetail(event.item)
            is HomeScreenEvent.OnFavoriteButtonClicked -> handleFavoritePlantAction(event.item)
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

    private fun handleFavoritePlantAction(plant: Plant) {
        coroutineScope.launch {
            val chosenPlant = try {
                plantsRepository.getFavoritePlantById(plant.id)
            } catch (err: NullPointerException) {
                null
            }
            chosenPlant?.let {
                plantsRepository.removeFromFavorites(it.id)
            } ?: run {
                plantsRepository.addToFavorites(plant)
            }
        }
    }
}
