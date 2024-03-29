package com.vml.tutorial.plantshop.plants.presentation.favourites.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.MR.images.ic_empty_fav
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.plants.presentation.favourites.FavoritesScreenState
import com.vml.tutorial.plantshop.plants.presentation.home.components.HomeScreenConstants
import com.vml.tutorial.plantshop.plants.presentation.plantList.ItemListEvent
import com.vml.tutorial.plantshop.plants.presentation.plantList.itemListGrid
import com.vml.tutorial.plantshop.ui.theme.Typography
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun FavouritesScreen(state: FavoritesScreenState, onEvent: (FavoritesScreenEvent) -> Unit) {
    Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
        Text(UiText.StringRes(MR.strings.favorites).asString(), style = Typography.headlineMedium)
        Spacer(modifier = Modifier.size(16.dp))
        if (state.favoritePlants.isEmpty()) {
            EmptyMessage()
        } else {
            LazyVerticalGrid(columns = GridCells.Fixed(FavoritesScreenConstants.GRID_COLUMN_COUNT)) {
                itemListGrid(
                    state.favoritePlants,
                    modifier = Modifier.padding(8.dp)
                ) { plantListEvent ->
                    onEvent(getFavoritesScreenEventFrom(plantListEvent))
                }
                item(span = { GridItemSpan(HomeScreenConstants.SINGLE_GRID_COLUMN_SPAN) }) {
                    Spacer(modifier = Modifier.size(96.dp))
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

@Composable
private fun EmptyMessage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(72.dp))
        Icon(
            painter = painterResource(ic_empty_fav),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = UiText.StringRes(MR.strings.background_image).asString(),
            modifier = Modifier.size(200.dp, 200.dp)
        )
        Text(
            text = UiText.StringRes(MR.strings.empty_favorites_title).asString(),
            textAlign = TextAlign.Center,
            style = Typography.titleLarge,
            color = Color.Black.copy(alpha = 0.6f),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = UiText.StringRes(MR.strings.empty_favorites_message).asString(),
            textAlign = TextAlign.Center,
            style = Typography.titleMedium,
            fontWeight = FontWeight.Normal,
        )
    }
}

object FavoritesScreenConstants {
    const val GRID_COLUMN_COUNT = 2
}
