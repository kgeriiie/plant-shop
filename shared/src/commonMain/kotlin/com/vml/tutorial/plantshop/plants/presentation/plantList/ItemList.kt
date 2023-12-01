package com.vml.tutorial.plantshop.plants.presentation.plantList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
    this.items(items = items, key = { item -> item.id}) { item ->
        Box(modifier = modifier.clip(RoundedCornerShape(16.dp)).background(Color.White).clickable {
            onClickEvent(ItemListEvent.OnClicked(item))
        }) {
            Column(modifier = Modifier.padding(8.dp)) {
                Box(contentAlignment = Alignment.TopCenter) {
                    PlantImage(
                        url = item.image, contentScale = ContentScale.Fit, modifier = Modifier.size(136.dp)
                    )
                    FavoriteButton(onClick = { onClickEvent(ItemListEvent.OnFavoriteButtonClicked(item)) })
                }
                ItemInfo(item.name, item.price)
            }
        }
    }
}

@Composable
private fun FavoriteButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    var isFavorite by remember { mutableStateOf(false) }
    Row {
        Spacer(modifier = Modifier.weight(1f))
        IconButton(modifier = Modifier.size(50.dp), onClick = {
            isFavorite = !isFavorite
            onClick()
        }) {
            Icon(
                imageVector = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onPrimaryContainer,
                contentDescription = UiText.StringRes(MR.strings.favorite_icon_description).asString()
            )
        }
    }
}

@Composable
private fun ItemInfo(name: String, price: String) {
    Text(name, style = Typography.bodyMedium)
    Text(price, style = Typography.titleMedium)
}
