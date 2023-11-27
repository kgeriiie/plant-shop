package com.vml.tutorial.plantshop.plants.presentation.home.components

import com.vml.tutorial.plantshop.plants.domain.Plant

sealed interface HomeScreenEvent {
    data object OnProfileClicked: HomeScreenEvent
    data class OnSearchClicked(val query: String): HomeScreenEvent
    data object OnOfferClicked: HomeScreenEvent  //Todo: To be decided later
    data class OnClicked(val item: Plant): HomeScreenEvent
    data class OnFavoriteButtonClicked(val item: Plant): HomeScreenEvent
}
