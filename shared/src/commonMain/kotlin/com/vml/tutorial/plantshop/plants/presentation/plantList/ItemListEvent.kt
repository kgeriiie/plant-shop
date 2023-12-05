package com.vml.tutorial.plantshop.plants.presentation.plantList

import com.vml.tutorial.plantshop.plants.domain.Plant
sealed interface ItemListEvent {
    data class OnFavoriteButtonClicked(val item: Plant): ItemListEvent
    data class OnClicked(val item: Plant): ItemListEvent
}
