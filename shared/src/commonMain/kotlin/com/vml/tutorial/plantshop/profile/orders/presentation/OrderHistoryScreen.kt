package com.vml.tutorial.plantshop.profile.orders.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderHistoryEvents
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderHistoryUiState
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderListItemUiState
import com.vml.tutorial.plantshop.rate.RateDialog
import com.vml.tutorial.plantshop.ui.theme.Typography
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun OrderHistoryScreen(
    state: OrderHistoryUiState,
    paddingHorizontal: Dp = 20.dp,
    paddingHorizontalOffset: Dp = 2.dp,
    onEvent: (event: OrderHistoryEvents) -> Unit,
) {
    state.commonState.confirmAction?.let { action ->
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

    if (state.displayRating) {
        RateDialog(
            orderId = state.commonState.ratingState?.orderId.orEmpty(),
            rating = state.commonState.ratingState?.previousRating,
            onSubmitRating = { rating ->
                onEvent(OrderHistoryEvents.OnRateSubmitted(rating))
            },
            onDismissRequest = {
                onEvent(OrderHistoryEvents.DismissRatingDialog)
            }
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

            if (state.commonState.contentLoading) {
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

    LaunchedEffect("init-screen") {
        onEvent(OrderHistoryEvents.FetchContents)
    }
}

@Composable
private fun OrderListSection(
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
private fun EmptyMessage(
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
        Image(
            painter = painterResource(MR.images.bg_empty_history),
            contentDescription = UiText.StringRes(MR.strings.background_image).asString(),
            modifier = Modifier.size(200.dp, 200.dp)
        )
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