package com.vml.tutorial.plantshop.plants.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
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
import com.vml.tutorial.plantshop.MR.strings.off
import com.vml.tutorial.plantshop.MR.strings.offer_image_description
import com.vml.tutorial.plantshop.MR.strings.profile_photo_description
import com.vml.tutorial.plantshop.MR.strings.search
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.PlantType
import com.vml.tutorial.plantshop.plants.presentation.home.HomeScreenEvent
import com.vml.tutorial.plantshop.plants.presentation.home.HomeScreenState
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
    val plants by remember { mutableStateOf(state.plants) }
    Scaffold {
        Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
            ScreenTitle({ onEvent(HomeScreenEvent.OnProfileClicked) })
            SearchBar(modifier = Modifier.padding(bottom = 8.dp),
                onSearchClicked = { query -> onEvent(HomeScreenEvent.OnSearchClicked(query)) })
            HomeScreenContent(plants = plants, onEvent = onEvent)
        }
    }
}

@Composable
private fun HomeScreenContent(
    plants: List<Plant>, modifier: Modifier = Modifier, onEvent: (HomeScreenEvent) -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Fixed(GRID_COLUMN_COUNT)) {
        item(span = { GridItemSpan(SINGLE_GRID_COLUMN_SPAN) }) {
            Column(
                modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CategorySelector { plantType ->
                    onEvent(HomeScreenEvent.OnCategoryClicked(plantType))
                }
                ProductOffer { onEvent(HomeScreenEvent.OnOfferClicked) }
            }
        }
        itemListGrid(plants, modifier = Modifier.padding(8.dp)) { plantListEvent ->
            onEvent(getHomeScreenEventFrom(plantListEvent))
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
    Row(modifier = modifier) {
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
private fun SearchBar(onSearchClicked: (String) -> Unit, modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf(DEFAULT_SEARCH_QUERY) }
    var active by remember { mutableStateOf(false) }

    SearchBar(modifier = modifier.fillMaxWidth(), query = query, onQueryChange = {
        query = it
    }, onSearch = {
        active = false
        onSearchClicked(it)
    }, active = active, onActiveChange = {
        active = it
    }, placeholder = {
        Text(text = UiText.StringRes(search).asString())
    }, trailingIcon = {
        Icon(imageVector = Icons.Default.Search, contentDescription = null)
    }) { }
}

@Composable
private fun CategorySelector(modifier: Modifier = Modifier, onClick: (PlantType) -> Unit) {
    Row {
        CategoryItem(
            ic_green_plant, UiText.StringRes(green_plants).asString(), Modifier.weight(1f)
        ) { onClick(PlantType.GREEN) }
        CategoryItem(
            ic_flower, UiText.StringRes(flowers).asString(), Modifier.weight(1f)
        ) { onClick(PlantType.FLOWER) }
        CategoryItem(
            ic_indoor_plant, UiText.StringRes(indoor_plants).asString(), Modifier.weight(1f)
        ) { onClick(PlantType.INDOOR) }
    }
}

@Composable
private fun CategoryItem(
    iconResource: ImageResource,
    categoryText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(modifier = modifier.padding(8.dp).clip(RoundedCornerShape(16.dp)).background(Color.White)
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
    Box {
        Image(painter = painterResource(img_offer),
            contentDescription = UiText.StringRes(offer_image_description).asString(),
            contentScale = ContentScale.FillWidth,
            modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                .clickable { onClick() })
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
    const val OFFER_PERCENTAGE = 20
}
