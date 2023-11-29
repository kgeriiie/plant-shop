package com.vml.tutorial.plantshop.plants.presentation.home.components

import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.PlantType

sealed interface HomeScreenEvent {
    data object OnProfileClicked: HomeScreenEvent
    data class OnSearchClicked(val query: String): HomeScreenEvent
    data class OnCategoryClicked(val plantType: PlantType): HomeScreenEvent
    data object OnOfferClicked: HomeScreenEvent
    data class OnClicked(val item: Plant): HomeScreenEvent
    data class OnFavoriteButtonClicked(val item: Plant): HomeScreenEvent
}
