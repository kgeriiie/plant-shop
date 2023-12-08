package com.vml.tutorial.plantshop.plants.presentation.plantList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.presentation.PlantImage
import com.vml.tutorial.plantshop.ui.theme.Typography

fun LazyGridScope.itemListGrid(
    items: List<Plant>, modifier: Modifier = Modifier, onClickEvent: (ItemListEvent) -> Unit
) {
    this.items(items = items, key = { item -> item.id }) { item ->
        val roundedCornerShape = remember { RoundedCornerShape(16.dp) }
        Box(modifier = modifier.shadow(elevation = 4.dp, shape = roundedCornerShape)
            .clip(roundedCornerShape).background(Color.White).clickable {
                onClickEvent(ItemListEvent.OnClicked(item))
            }) {
            Column {
                Box(contentAlignment = Alignment.TopCenter) {
                    PlantImage(
                        url = item.image,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(136.dp)
                    )
                    FavoriteButton(item.isFavorite, onClick = {
                        item.isFavorite = !item.isFavorite
                        onClickEvent(ItemListEvent.OnFavoriteButtonClicked(item))
                    })
                }
                ItemInfo(name = item.name, price = item.price, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
private fun FavoriteButton(
    isInFavorites: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(isInFavorites) }
    Row(modifier = modifier) {
        Spacer(modifier = Modifier.weight(1f))
        IconButton(modifier = Modifier.size(50.dp), onClick = {
            isFavorite = !isFavorite
            onClick()
        }) {
            Icon(
                imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onPrimaryContainer,
                contentDescription = UiText.StringRes(MR.strings.favorite_icon_description)
                    .asString()
            )
        }
    }
}

@Composable
private fun ItemInfo(name: String, price: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(name, style = Typography.bodyMedium)
        Text(price, style = Typography.titleMedium)
    }
}
