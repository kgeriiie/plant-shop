package com.vml.tutorial.plantshop.profile.orders.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.MR.strings.orders_active_orders_title_text
import com.vml.tutorial.plantshop.MR.strings.orders_cancelled_orders_title_text
import com.vml.tutorial.plantshop.MR.strings.orders_completed_orders_title_text
import com.vml.tutorial.plantshop.core.presentation.DefaultDialog
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.core.utils.exts.orZero
import com.vml.tutorial.plantshop.plants.presentation.PlantImage
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderHistoryEvents
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderHistoryUiState
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderListItemUiState
import com.vml.tutorial.plantshop.ui.theme.Typography

@Composable
fun OrderHistoryScreen(
    state: OrderHistoryUiState,
    paddingHorizontal: Dp = 20.dp,
    paddingHorizontalOffset: Dp = 2.dp,
    onEvent: (event: OrderHistoryEvents) -> Unit,
) {
    state.confirmAction?.let { action ->
        DefaultDialog(
            title = UiText.StringRes(MR.strings.confirm_text).asString(),
            message = action.message.asString(),
            primaryText = UiText.StringRes(MR.strings.yes_text).asString(),
            primaryCallback = { onEvent(OrderHistoryEvents.ConfirmDialogDismissed(action, true)) },
            secondaryText = UiText.StringRes(MR.strings.no_text).asString(),
            secondaryCallback = { onEvent(OrderHistoryEvents.ConfirmDialogDismissed(action, false)) },
            onDismissRequest = { onEvent(OrderHistoryEvents.ConfirmDialogDismissed(action, false)) }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                modifier = Modifier.size(50.dp),
                onClick = {
                    onEvent(OrderHistoryEvents.NavigateBack)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = UiText.StringRes(MR.strings.orders_screen_title).asString(),
                style = Typography.headlineMedium
            )

            if (state.contentLoading) {
                Spacer(modifier = Modifier.weight(1f))
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        if (state.displayEmptyMessage) {
            EmptyMessage {
                onEvent(OrderHistoryEvents.StartOrderPressed)
            }
        } else {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                state.pendingOrders.takeIf { it.isNotEmpty() }?.let { orders ->
                    OrderListSection(
                        title = UiText.StringRes(orders_active_orders_title_text),
                        modifier = Modifier.fillMaxWidth(),
                        orders = orders,
                        paddingHorizontal = paddingHorizontal,
                        paddingHorizontalOffset = paddingHorizontalOffset
                    ) { event ->
                        onEvent(event)
                    }
                }

                state.shippedOrders.takeIf { it.isNotEmpty() }?.let { orders ->
                    OrderListSection(
                        title = UiText.StringRes(orders_completed_orders_title_text),
                        modifier = Modifier.fillMaxWidth(),
                        orders = orders,
                        paddingHorizontal = paddingHorizontal,
                        paddingHorizontalOffset = paddingHorizontalOffset
                    ) { event ->
                        onEvent(event)
                    }
                }

                state.cancelledOrders.takeIf { it.isNotEmpty() }?.let { orders ->
                    OrderListSection(
                        title = UiText.StringRes(orders_cancelled_orders_title_text),
                        modifier = Modifier.fillMaxWidth(),
                        orders = orders,
                        paddingHorizontal = paddingHorizontal,
                        paddingHorizontalOffset = paddingHorizontalOffset
                    ) { event ->
                        onEvent(event)
                    }
                }
                Spacer(modifier = Modifier.height(45.dp))
            }
        }

    }
}

@Composable
fun OrderListSection(
    title: UiText,
    orders: List<OrderListItemUiState>,
    modifier: Modifier = Modifier,
    paddingHorizontal: Dp,
    paddingHorizontalOffset: Dp,
    onEvent: (event: OrderHistoryEvents) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = paddingHorizontal + paddingHorizontalOffset)
        ) {
            Text(
                text = title.asString(),
                fontSize = 20.sp,
                style = Typography.titleLarge
            )
            if (orders.size > 1) {
                TextButton(
                    onClick = {
                        onEvent(OrderHistoryEvents.ShowAllPressed(orders.first().data.status))
                    }
                ) {
                    Text(
                        text = UiText.StringRes(MR.strings.orders_view_all_text).asString(),
                        style = Typography.labelLarge
                    )
                }
            }
        }

        LazyRow(
            modifier = Modifier,
            contentPadding = PaddingValues(horizontal = paddingHorizontal),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                orders.size.orZero(),
                key = { orders[it].data.orderNumber }
            ) {index ->
                OrderListItem(
                    itemState = orders[index],
                    modifier = Modifier
                        .width(320.dp)
                        .padding(vertical = 10.dp)
                ) {
                    onEvent(it)
                }
            }
        }
    }
}

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
                val imageWidth = containerWidth.times(0.7f)
                val offset = containerWidth.minus(imageWidth).div(itemState.standalonePlants.size.minus(1))

                LazyRow(
                    modifier = Modifier
                        .size(containerWidth, 100.dp),
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
                        color = Color.Black.copy(alpha = 0.6f),
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
                Button(
                    onClick = { onEvent(OrderHistoryEvents.PrimaryButtonPressed(itemState.data)) },
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    Text(
                        text = itemState.primaryButtonTitle.asString()
                    )
                }

                Spacer(Modifier.width(10.dp))
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

@Composable
fun EmptyMessage(
    modifier: Modifier = Modifier,
    onClick: ()-> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(120.dp))
        Text(
            text = UiText.StringRes(MR.strings.orders_empty_title_text).asString(),
            textAlign = TextAlign.Center,
            style = Typography.titleLarge,
            color = Color.Black.copy(alpha = 0.6f),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = UiText.StringRes(MR.strings.orders_empty_message_text).asString(),
            textAlign = TextAlign.Center,
            style = Typography.titleMedium,
            fontWeight =FontWeight.Normal,
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onClick
        ) {
            Text(
                text = UiText.StringRes(MR.strings.orders_empty_action_button_text).asString(),
                style = Typography.titleSmall
            )
        }
    }
}