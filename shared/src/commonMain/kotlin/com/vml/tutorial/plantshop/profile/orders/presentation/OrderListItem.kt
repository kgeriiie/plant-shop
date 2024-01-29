package com.vml.tutorial.plantshop.profile.orders.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.plants.presentation.PlantImage
import com.vml.tutorial.plantshop.profile.orders.domain.OrderStatus
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderHistoryEvents
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderListItemUiState
import com.vml.tutorial.plantshop.ui.theme.Typography

@Composable
fun OrderListItem(
    itemState: OrderListItemUiState,
    modifier: Modifier = Modifier,
    onEvent: (event: OrderHistoryEvents) -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val containerWidth: Dp = 100.dp.takeIf { itemState.standalonePlants.size == 1 }?: 120.dp
                val imageWidth = containerWidth.times(1f.takeIf { itemState.standalonePlants.size == 1 }?: 0.7f)
                val offset = 0.dp.takeIf { itemState.standalonePlants.size == 1 }?: containerWidth.minus(imageWidth).div(itemState.standalonePlants.size.minus(1))

                LazyRow(
                    modifier = Modifier
                        .size(width = containerWidth, height = 120.dp),
                    horizontalArrangement = Arrangement.spacedBy(-(imageWidth.minus(offset)))
                ) {
                    items(
                        count = itemState.standalonePlants.size,
                        key = {
                            itemState.standalonePlants[it].id.toString()
                        }
                    ) {index ->
                        itemState.standalonePlants.getOrNull(index)?.let { plant ->
                            Box(
                                modifier = Modifier
                                    .width(imageWidth)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(4.dp))
                            ) {
                                PlantImage(
                                    url = plant.image,
                                    modifier = Modifier
                                        .width(imageWidth)
                                        .fillMaxHeight()
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = itemState.orderId.asString(),
                        style = Typography.titleSmall,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Light,
                        color = Color.Black.copy(alpha = 0.6f),
                    )
                    Text(
                        text = itemState.title.asString(),
                        style = Typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black.copy(alpha = 0.6f),
                        modifier = Modifier.height(50.dp)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = itemState.subTitle.asString(),
                        style = Typography.titleSmall,
                        color = Color.Gray.copy(alpha = 0.9f),
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = itemState.totalPrice.asString(),
                        style = Typography.titleMedium,
                        color = Color.Black.copy(alpha = 0.6f),
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (itemState.data.status != OrderStatus.CANCELLED) {
                    OutlinedButton(
                        onClick = { onEvent(OrderHistoryEvents.PrimaryButtonPressed(itemState.data)) },
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    ) {
                        Text(
                            text = itemState.primaryButtonTitle.asString()
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                } else {
                    Spacer(Modifier.fillMaxWidth().weight(1f))
                }

                Button(
                    onClick = { onEvent(OrderHistoryEvents.SecondaryButtonPressed(itemState.data)) },
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    Text(
                        text = itemState.secondaryButtonTitle.asString()
                    )
                }
            }
        }
    }
}