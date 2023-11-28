package com.vml.tutorial.plantshop.plants.presentation.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DeviceThermostat
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Rule
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Straighten
import androidx.compose.material.icons.rounded.WaterDamage
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vml.tutorial.plantshop.MR.strings.checkout_with_price
import com.vml.tutorial.plantshop.MR.strings.plant_info_drained_text
import com.vml.tutorial.plantshop.MR.strings.plant_info_full_sun_text
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.plants.domain.Plant
import com.vml.tutorial.plantshop.plants.domain.PlantDetails
import com.vml.tutorial.plantshop.plants.presentation.PlantImage
import com.vml.tutorial.plantshop.plants.presentation.detail.PlantDetailEvent
import com.vml.tutorial.plantshop.plants.presentation.detail.PlantDetailState

@Composable
fun PlantDetailScreen(
    state: PlantDetailState,
    onEvent: (PlantDetailEvent) -> Unit
) {
    Scaffold {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // COVER IMAGE
            PlantImage(
                url = state.plant.image,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.6f)
            )

            // TOOLBAR
            PlantDetailToolbar(
                isFavourite = state.isFavourite,
                onEvent = onEvent
            )

            PlantDetails(
                plant = state.plant,
                quantity = state.quantity,
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun PlantDetailToolbar(
    isFavourite: Boolean,
    onEvent: (PlantDetailEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        // BACK
        IconButton(
            modifier = Modifier.size(50.dp),
            onClick = {
                onEvent(PlantDetailEvent.NavigateBack)
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.weight(1.0f))

        // SHARE
        IconButton(
            modifier = Modifier.size(50.dp),
            onClick = {
                onEvent(PlantDetailEvent.OnShareClick)
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.Share,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        // FAVORITE
        IconButton(
            modifier = Modifier.size(50.dp),
            onClick = {
                onEvent(PlantDetailEvent.OnFavouriteClick)
            }
        ) {
            Icon(
                imageVector = if (isFavourite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun PlantDetails(
    plant: Plant,
    quantity: Int,
    onEvent: (PlantDetailEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(4.dp)
                    .width(40.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.Gray.copy(alpha = 0.8f))
                    .align(Alignment.CenterHorizontally),
            )

            // PLANT NAME
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = plant.name,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Text(
                    text = plant.originalName,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }

            // PLANT DESCRIPTION
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = plant.description,
                color = Color.Gray,
                fontSize = 14.sp
            )

            // PLANT DETAILS INFO
            Spacer(modifier = Modifier.height(24.dp))
            PlantExtraInfoRow(plant.details)

            // PLANT QUANTITY
            Spacer(modifier = Modifier.height(24.dp))
            Row() {
                QuantityController(
                    value = quantity,
                    onQuantityChanged = { quantity ->
                        onEvent(PlantDetailEvent.OnQuantityChanged(quantity))
                    }
                )
                Spacer(modifier = Modifier.weight(1.0f))
                Button(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.primary),
                    onClick = {
                        onEvent(PlantDetailEvent.CheckoutPlant)
                    }
                ) {
                    Text(
                        text = UiText.StringRes(checkout_with_price, listOf(plant.price)).asString(),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PlantExtraInfoRow(
    details: PlantDetails
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
    ) {
        if (details.drained) {
            PlantInfo(
                icon = Icons.Rounded.WaterDrop,
                text = UiText.StringRes(plant_info_drained_text).asString()
            )
        }
        if (details.fullSun) {
            Spacer(modifier = Modifier.width(6.dp))
            PlantInfo(
                icon = Icons.Rounded.WbSunny,
                text = UiText.StringRes(plant_info_full_sun_text).asString()
            )
        }

        Spacer(modifier = Modifier.width(6.dp))
        PlantInfo(
            icon = Icons.Rounded.Straighten,
            text = details.size
        )

        Spacer(modifier = Modifier.width(6.dp))
        PlantInfo(
            icon = Icons.Rounded.DeviceThermostat,
            text = details.temperature
        )
    }
}

@Composable
fun PlantInfo(
    icon: ImageVector,
    text: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .size(80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = text,
                color = Color.Black,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun QuantityController(
    value: Int,
    onQuantityChanged: (quantity: Int) -> Unit
) {
    Row(
        modifier = Modifier.height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.size(48.dp),
            onClick = {
                onQuantityChanged(value.dec())
            }
        ) {
            Box(
                modifier = Modifier.size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.LightGray)
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.Center),
                    imageVector = Icons.Rounded.Remove,
                    contentDescription = null,
                )
            }
        }
        Text(
            text = "$value",
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(50.dp)
        )
        IconButton(
            modifier = Modifier
                .size(48.dp),
            onClick = {
                onQuantityChanged(value.inc())
            }
        ) {
            Box(
                modifier = Modifier.size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.LightGray)
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.Center),
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                )
            }
        }
    }
}