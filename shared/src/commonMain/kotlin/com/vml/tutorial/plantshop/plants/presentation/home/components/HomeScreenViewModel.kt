package com.vml.tutorial.plantshop.plants.presentation.home.components

import com.vml.tutorial.plantshop.plants.domain.Plant
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeScreenViewModel(plants: List<Plant>) : ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState(plants))
    val state: StateFlow<HomeScreenState> = _state.asStateFlow()

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.OnClicked -> TODO()
            is HomeScreenEvent.OnFavoriteButtonClicked -> TODO()
            HomeScreenEvent.OnOfferClicked -> TODO()
            HomeScreenEvent.OnProfileClicked -> TODO()
            is HomeScreenEvent.OnSearchClicked -> TODO()
        }
    }
}