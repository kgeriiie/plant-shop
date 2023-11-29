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
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.MR.images.ic_flower
import com.vml.tutorial.plantshop.MR.images.ic_green_plant
import com.vml.tutorial.plantshop.MR.images.ic_indoor_plant
import com.vml.tutorial.plantshop.MR.images.img_offer
import com.vml.tutorial.plantshop.MR.strings.discover
import com.vml.tutorial.plantshop.MR.strings.flowers
import com.vml.tutorial.plantshop.MR.strings.green_plants
import com.vml.tutorial.plantshop.MR.strings.indoor_plants
import com.vml.tutorial.plantshop.MR.strings.offer_image_description
import com.vml.tutorial.plantshop.MR.strings.profile_photo_description
import com.vml.tutorial.plantshop.MR.strings.search
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.PlantType
import com.vml.tutorial.plantshop.ui.theme.Typography
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun HomeScreen(state: HomeScreenState, onEvent: (HomeScreenEvent) -> Unit) {
    Scaffold {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(it).padding(16.dp)
        ) {
            ProfileTitle({
                onEvent(HomeScreenEvent.OnProfileClicked)
            })
            SearchBar({ query ->
                onEvent(HomeScreenEvent.OnSearchClicked(query))
            })
            CategorySelector { plantType ->
                onEvent(HomeScreenEvent.OnCategoryClicked(plantType))
            }
            ProductOffer {
                onEvent(HomeScreenEvent.OnOfferClicked)
            }
            PlantList(state.plants)
        }
    }
}

@Composable
private fun ProfileTitle(onProfileClicked: () -> Unit, modifier: Modifier = Modifier) {
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
    var text by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    SearchBar(modifier = modifier.fillMaxWidth(), query = text, onQueryChange = {
        text = it
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
        categoryItem(
            ic_green_plant, UiText.StringRes(green_plants).asString(), Modifier.weight(1f)
        ) { onClick(PlantType.GREEN) }
        categoryItem(
            ic_flower, UiText.StringRes(flowers).asString(), Modifier.weight(1f)
        ) { onClick(PlantType.FLOWER) }
        categoryItem(
            ic_indoor_plant, UiText.StringRes(indoor_plants).asString(), Modifier.weight(1f)
        ) { onClick(PlantType.INDOOR) }
    }
}

@Composable
private fun categoryItem(
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
    Image(painter = painterResource(img_offer),
        contentDescription = UiText.StringRes(offer_image_description).asString(),
        contentScale = ContentScale.FillWidth,
        modifier = modifier.fillMaxWidth().clip(
            RoundedCornerShape(16.dp)
        ).clickable { onClick() })
}

@Composable
private fun PlantList(plants: List<Plant>, modifier: Modifier = Modifier) {

}

