package com.vml.tutorial.plantshop.plants.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.MR.images.ic_flower
import com.vml.tutorial.plantshop.MR.images.ic_green_plant
import com.vml.tutorial.plantshop.MR.images.ic_indoor_plant
import com.vml.tutorial.plantshop.MR.images.img_offer
import com.vml.tutorial.plantshop.MR.strings.discount_percentage
import com.vml.tutorial.plantshop.MR.strings.discover
import com.vml.tutorial.plantshop.MR.strings.flowers
import com.vml.tutorial.plantshop.MR.strings.for_today
import com.vml.tutorial.plantshop.MR.strings.get
import com.vml.tutorial.plantshop.MR.strings.green_plants
import com.vml.tutorial.plantshop.MR.strings.indoor_plants
import com.vml.tutorial.plantshop.MR.strings.no_search_result
import com.vml.tutorial.plantshop.MR.strings.off
import com.vml.tutorial.plantshop.MR.strings.offer_image_description
import com.vml.tutorial.plantshop.MR.strings.profile_photo_description
import com.vml.tutorial.plantshop.MR.strings.search
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.PlantCategory
import com.vml.tutorial.plantshop.plants.presentation.home.HomeScreenEvent
import com.vml.tutorial.plantshop.plants.presentation.home.HomeScreenState
import com.vml.tutorial.plantshop.plants.presentation.home.components.HomeScreenConstants.DEFAULT_SEARCH_ACTIVE_STATE
import com.vml.tutorial.plantshop.plants.presentation.home.components.HomeScreenConstants.DEFAULT_SEARCH_QUERY
import com.vml.tutorial.plantshop.plants.presentation.home.components.HomeScreenConstants.GRID_COLUMN_COUNT
import com.vml.tutorial.plantshop.plants.presentation.home.components.HomeScreenConstants.OFFER_PERCENTAGE
import com.vml.tutorial.plantshop.plants.presentation.home.components.HomeScreenConstants.SINGLE_GRID_COLUMN_SPAN
import com.vml.tutorial.plantshop.plants.presentation.plantList.ItemListEvent
import com.vml.tutorial.plantshop.plants.presentation.plantList.itemListGrid
import com.vml.tutorial.plantshop.ui.theme.Typography
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun HomeScreen(state: HomeScreenState, onEvent: (HomeScreenEvent) -> Unit) {
    Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
        ScreenTitle({ onEvent(HomeScreenEvent.OnProfileClicked) })
        SearchBar(
            state.searchResults,
            onSearchEvent = onEvent,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        HomeScreenContent(
            plants = state.plants,
            chosenCategory = state.chosenCategory,
            onEvent = onEvent
        )
    }
}

@Composable
private fun HomeScreenContent(
    plants: List<Plant>?,
    chosenCategory: PlantCategory,
    modifier: Modifier = Modifier,
    onEvent: (HomeScreenEvent) -> Unit
) {
    LazyVerticalGrid(modifier = modifier, columns = GridCells.Fixed(GRID_COLUMN_COUNT)) {
        item(span = { GridItemSpan(SINGLE_GRID_COLUMN_SPAN) }) {
            Column {
                Spacer(modifier = Modifier.size(8.dp))
                CategorySelector(
                    modifier = Modifier.padding(8.dp),
                    chosenCategory = chosenCategory
                ) { plantType ->
                    onEvent(HomeScreenEvent.OnCategoryClicked(plantType))
                }
                ProductOffer(modifier = Modifier.padding(8.dp)) {
                    onEvent(HomeScreenEvent.OnOfferClicked)
                }
            }
        }
        when {
            plants.isNullOrEmpty() -> {
                item(span = { GridItemSpan(SINGLE_GRID_COLUMN_SPAN) }) {
                    Text(
                        text = if (plants == null) {
                            UiText.StringRes(MR.strings.error)
                        } else {
                            UiText.StringRes(MR.strings.empty_plant_list)
                        }.asString(),
                        modifier = Modifier.padding(top = 64.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {
                itemListGrid(
                    plants,
                    modifier = Modifier.padding(8.dp)
                ) { plantListEvent ->
                    onEvent(getHomeScreenEventFrom(plantListEvent))
                }
            }
        }
    }
}

private fun getHomeScreenEventFrom(plantListEvent: ItemListEvent): HomeScreenEvent {
    return when (plantListEvent) {
        is ItemListEvent.OnClicked -> HomeScreenEvent.OnItemClicked(plantListEvent.item)
        is ItemListEvent.OnFavoriteButtonClicked -> HomeScreenEvent.OnFavoriteButtonClicked(
            plantListEvent.item
        )
    }
}

@Composable
private fun ScreenTitle(onProfileClicked: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(UiText.StringRes(discover).asString(), style = Typography.headlineMedium)
        Spacer(modifier = Modifier.weight(1.0f))
        IconButton(
            onClick = { onProfileClicked() },
            modifier = Modifier.border(1.dp, Color.Black, shape = CircleShape).size(32.dp)
        ) {
            Icon(
                Icons.Rounded.Person,
                contentDescription = UiText.StringRes(profile_photo_description).asString()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    searchResults: List<Plant>,
    modifier: Modifier = Modifier,
    onSearchEvent: (HomeScreenEvent) -> Unit
) {
    var query by remember { mutableStateOf(DEFAULT_SEARCH_QUERY) }
    var isActive by remember { mutableStateOf(DEFAULT_SEARCH_ACTIVE_STATE) }

    SearchBar(modifier = modifier.fillMaxWidth(), query = query, onQueryChange = {
        query = it
        onSearchEvent(HomeScreenEvent.OnSearchQueryChanged(it))
    }, onSearch = {
        isActive = false
    }, active = query.isNotBlank() && isActive, onActiveChange = {
        if (!it) {
            query = DEFAULT_SEARCH_QUERY
        }
        isActive = it
    }, placeholder = {
        Text(text = UiText.StringRes(search).asString())
    }, trailingIcon = {
        Icon(imageVector = Icons.Default.Search, contentDescription = null)
    }) {
        SearchResult(searchResults = searchResults, onSearchEvent = onSearchEvent)
    }
}

@Composable
private fun SearchResult(
    searchResults: List<Plant>,
    modifier: Modifier = Modifier,
    onSearchEvent: (HomeScreenEvent) -> Unit
) {
    if (searchResults.isNotEmpty()) {
        LazyColumn(modifier = modifier, contentPadding = PaddingValues(horizontal = 8.dp)) {
            items(searchResults) { plant ->
                Text(text = plant.name, modifier = Modifier.padding(8.dp).fillMaxWidth().clickable {
                    onSearchEvent(HomeScreenEvent.OnResultItemClicked(plant))
                })
            }
        }
    } else {
        Text(
            text = UiText.StringRes(no_search_result).asString(),
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
private fun CategorySelector(
    modifier: Modifier = Modifier,
    chosenCategory: PlantCategory,
    onClick: (PlantCategory) -> Unit
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        CategoryItem(
            ic_green_plant,
            UiText.StringRes(green_plants).asString(),
            chosenCategory == PlantCategory.GREEN,
            Modifier.weight(1f)
        ) { onClick(PlantCategory.GREEN) }
        CategoryItem(
            ic_flower,
            UiText.StringRes(flowers).asString(),
            chosenCategory == PlantCategory.FLOWER,
            Modifier.weight(1f)
        ) { onClick(PlantCategory.FLOWER) }
        CategoryItem(
            ic_indoor_plant,
            UiText.StringRes(indoor_plants).asString(),
            chosenCategory == PlantCategory.INDOOR,
            Modifier.weight(1f)
        ) { onClick(PlantCategory.INDOOR) }
    }
}

@Composable
private fun CategoryItem(
    iconResource: ImageResource,
    categoryText: String,
    isChosen: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val roundedCornerShape = remember { RoundedCornerShape(16.dp) }
    val backgroundColor = if (isChosen) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        Color.White
    }

    Box(modifier = modifier.shadow(elevation = 4.dp, shape = roundedCornerShape)
        .clip(roundedCornerShape)
        .background(backgroundColor)
        .clickable { onClick() }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Icon(
                painterResource(iconResource),
                contentDescription = categoryText,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                categoryText,
                style = Typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ProductOffer(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(modifier = modifier.clip(RoundedCornerShape(16.dp)).clickable { onClick() }) {
        Image(
            painter = painterResource(img_offer),
            contentDescription = UiText.StringRes(offer_image_description).asString(),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
        Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 32.dp)) {
            Text(text = UiText.StringRes(get).asString(), style = Typography.bodyMedium)
            Text(buildAnnotatedString {
                withStyle(style = Typography.headlineLarge.toSpanStyle()) {
                    append(
                        text = UiText.StringRes(discount_percentage, listOf(OFFER_PERCENTAGE))
                            .asString()
                    )
                }
                withStyle(style = Typography.titleLarge.toSpanStyle()) {
                    append(text = UiText.StringRes(off).asString())
                }
            })
            Text(text = UiText.StringRes(for_today).asString(), style = Typography.bodyMedium)
        }
    }
}

object HomeScreenConstants {
    const val GRID_COLUMN_COUNT = 2
    const val SINGLE_GRID_COLUMN_SPAN = GRID_COLUMN_COUNT
    const val DEFAULT_SEARCH_QUERY = ""
    const val DEFAULT_SEARCH_ACTIVE_STATE = false
    const val OFFER_PERCENTAGE = 20
}
