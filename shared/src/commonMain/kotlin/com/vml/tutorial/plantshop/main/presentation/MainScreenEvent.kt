package com.vml.tutorial.plantshop.main.presentation

sealed interface MainScreenEvent {
    data object OnHomeTabClicked: MainScreenEvent
    data object OnFavouriteTabClicked: MainScreenEvent
    data object OnBasketTabClicked: MainScreenEvent
}