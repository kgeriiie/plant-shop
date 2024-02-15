package com.vml.tutorial.plantshop.plants.presentation.favourites.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.plants.presentation.favourites.FavoritesScreenState
import com.vml.tutorial.plantshop.plants.presentation.plantList.ItemListEvent
import com.vml.tutorial.plantshop.plants.presentation.plantList.itemListGrid
import com.vml.tutorial.plantshop.ui.theme.Typography

@Composable
fun FavouritesScreen(state: FavoritesScreenState, onEvent: (FavoritesScreenEvent) -> Unit) {
    Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
        Text(UiText.StringRes(MR.strings.favorites).asString(), style = Typography.headlineMedium)
        Spacer(modifier = Modifier.size(16.dp))
        if (state.favoritePlants.isEmpty()) {
            Text(
                text = UiText.StringRes(MR.strings.empty_favorites_list).asString(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally).padding(top = 136.dp),
                textAlign = TextAlign.Center
            )
        } else {
            LazyVerticalGrid(columns = GridCells.Fixed(FavoritesScreenConstants.GRID_COLUMN_COUNT)) {
                itemListGrid(
                    state.favoritePlants,
                    modifier = Modifier.padding(8.dp)
                ) { plantListEvent ->
                    onEvent(getFavoritesScreenEventFrom(plantListEvent))
                }
            }
        }
    }
}

private fun getFavoritesScreenEventFrom(plantListEvent: ItemListEvent): FavoritesScreenEvent {
    return when (plantListEvent) {
        is ItemListEvent.OnClicked -> FavoritesScreenEvent.OnItemClicked(plantListEvent.item)
        is ItemListEvent.OnFavoriteButtonClicked -> FavoritesScreenEvent.OnFavoriteButtonClicked(
            plantListEvent.item
        )
    }
}

object FavoritesScreenConstants {
    const val GRID_COLUMN_COUNT = 2
}