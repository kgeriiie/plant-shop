package com.vml.tutorial.plantshop.plants.presentation.home

import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.PlantCategory

sealed interface HomeScreenEvent {
    data object OnProfileClicked: HomeScreenEvent
    data class OnSearchQueryChanged(val query: String): HomeScreenEvent
    data class OnResultItemClicked(val item: Plant): HomeScreenEvent
    data class OnCategoryClicked(val plantCategory: PlantCategory): HomeScreenEvent
    data object OnOfferClicked: HomeScreenEvent
    data class OnItemClicked(val item: Plant): HomeScreenEvent
    data class OnFavoriteButtonClicked(val item: Plant): HomeScreenEvent
}
