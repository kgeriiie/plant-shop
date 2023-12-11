package com.vml.tutorial.plantshop.plants.presentation.home

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.plants.data.PlantsRepository
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.PlantType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeScreenComponent(
    componentContext: ComponentContext,
    private val plantsRepository: PlantsRepository,
    private val onNavigateToDetail: (plant: Plant) -> Unit
) : ComponentContext by componentContext {

    private val _state = MutableStateFlow(HomeScreenState(plantsRepository.getPlants()))
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()


    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnItemClicked -> onNavigateToDetail(event.item)
            is HomeScreenEvent.OnFavoriteButtonClicked -> plantsRepository.toggleFavoriteStatus(
                event.item
            )

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
