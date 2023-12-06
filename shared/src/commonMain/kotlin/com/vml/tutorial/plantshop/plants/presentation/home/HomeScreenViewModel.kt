package com.vml.tutorial.plantshop.plants.presentation.home

import com.vml.tutorial.plantshop.plants.domain.PlantsDataSource
import com.vml.tutorial.plantshop.plants.presentation.PlantType
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeScreenViewModel(private val dataSource: PlantsDataSource) : ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState(dataSource.getPlants()))
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnItemClicked -> Unit //TODO()
            is HomeScreenEvent.OnFavoriteButtonClicked -> Unit //TODO()
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
