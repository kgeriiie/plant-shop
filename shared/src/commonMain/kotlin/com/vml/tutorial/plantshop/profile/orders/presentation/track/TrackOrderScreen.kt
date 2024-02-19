package com.vml.tutorial.plantshop.profile.orders.presentation.track

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.MR.strings.orders_item_order_id_text
import com.vml.tutorial.plantshop.MR.strings.track_delivery_address_title
import com.vml.tutorial.plantshop.MR.strings.track_estimated_delivery_title
import com.vml.tutorial.plantshop.MR.strings.track_home_address_title
import com.vml.tutorial.plantshop.core.presentation.DefaultDialog
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.core.utils.exts.dpToPx
import com.vml.tutorial.plantshop.profile.orders.domain.OrderActionState
import com.vml.tutorial.plantshop.profile.orders.domain.icon
import com.vml.tutorial.plantshop.profile.orders.domain.subtitle
import com.vml.tutorial.plantshop.profile.orders.domain.title
import com.vml.tutorial.plantshop.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun TrackOrderScreen(
    state: TrackOrderUiState,
    onEvent: (event: TrackOrderEvents) -> Unit,
) {
    state.confirmStateCompletion?.let { item ->
        DefaultDialog(
            title = UiText.StringRes(MR.strings.confirm_text).asString(),
            message = UiText.StringRes(MR.strings.track_confirm_completion_message, listOf(item.title.asString())).asString(),
            primaryText = UiText.StringRes(MR.strings.yes_text).asString(),
            primaryCallback = { onEvent(TrackOrderEvents.ConfirmDialogDismissed(item, true)) },
            secondaryText = UiText.StringRes(MR.strings.no_text).asString(),
            secondaryCallback = { onEvent(TrackOrderEvents.ConfirmDialogDismissed(item, false)) },
            onDismissRequest = { onEvent(TrackOrderEvents.ConfirmDialogDismissed(item, false)) }
        )
    }

    Column {
        // Toolbar
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                modifier = Modifier.size(50.dp),
                onClick = {
                    onEvent(TrackOrderEvents.ComponentEvents.NavigateBack)
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = UiText.StringRes(MR.strings.track_screen_title).asString(),
                style = Typography.headlineMedium
            )
        }

        // Order details
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            OrderDetails(
                orderDate = state.orderedDate,
                orderId = state.order.orderNumber,
                deliveryDate = state.estimatedDeliveryDate,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            state.details.forEachIndexed { index, item ->
                if (item.completed) {
                    Completed(
                        title = item.detail.actionState.title.asString(),
                        subtitle = item.detail.actionState.subtitle.asString(),
                        date = item.date.asString(),
                        icon = item.detail.actionState.icon,
                        visibleTop = index != 0,
                        visibleBottom = index != state.details.lastIndex,
                        filledBottom = !item.lastCompleted)
                } else {
                    Pending(
                        state = item.detail.actionState,
                        title = item.detail.actionState.title.asString(),
                        subtitle = item.detail.actionState.subtitle.asString(),
                        icon = item.detail.actionState.icon,
                        visibleTop = index != 0,
                        visibleBottom = index != state.details.lastIndex,
                    ) { state ->
                        onEvent(TrackOrderEvents.StateDidPressed(state))
                    }
                }
            }

            state.deliveryAddress?.let { address ->
                DeliveryAddress(
                    address,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun OrderDetails(
    modifier: Modifier = Modifier,
    orderDate: String,
    orderId: String,
    deliveryDate: String
) {
    Column(
        modifier = modifier
    ) {
        Row {
            Text(
                text = orderDate,
                style = Typography.titleSmall,
                color = Color.Gray.copy(alpha = 0.8f)
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = UiText.StringRes(track_estimated_delivery_title).asString(),
                style = Typography.titleMedium,
                color = Color.Black.copy(alpha = 0.6f),
            )
        }

        Row {
            Text(
                text = UiText.StringRes(orders_item_order_id_text, listOf(orderId)).asString(),
                style = Typography.titleSmall,
                color = Color.Gray.copy(alpha = 0.8f)
            )
            Spacer(Modifier.weight(1f))
            Text(
                deliveryDate,
                style = Typography.labelMedium,
                color = Color.Black.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun Pending(
    state: OrderActionState,
    title: String,
    subtitle: String,
    icon: ImageVector,
    visibleTop: Boolean = true,
    visibleBottom: Boolean = true,
    onStatePressed: (state: OrderActionState) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    var tapped by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.width(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalLine(top = true, filled = false, visibleTop, height = 35.dp)
            Box(modifier = Modifier
                .indication(interactionSource, rememberRipple(bounded = false))
                .size(30.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.Gray)
                .pointerInput(state) {
                    detectTapGestures(
                        onLongPress = { offset ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            tapped = true
                            val press = PressInteraction.Press(offset)
                            coroutineScope.launch {
                                interactionSource.emit(press)
                                interactionSource.emit(PressInteraction.Release(press))
                            }
                            tapped = false

                            onStatePressed(state)
                        }
                    )
                }
            )
            VerticalLine(top = false, filled = false, visibleBottom, height = 35.dp)
        }

        Icon(
            modifier = Modifier
                .size(32.dp),
            imageVector = icon,
            contentDescription = null,
            tint = Color.LightGray
        )
        Spacer(Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                style = Typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black.copy(alpha = 0.6f)
            )
            Text(
                text = subtitle,
                style = Typography.labelMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
private fun Completed(
    title: String,
    subtitle: String,
    date: String?,
    icon: ImageVector,
    visibleTop: Boolean = true,
    visibleBottom: Boolean = true,
    filledTop: Boolean = true,
    filledBottom: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.width(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalLine(top = true, filledTop, visibleTop)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.Center),
                    imageVector = Icons.Rounded.Done,
                    contentDescription = null
                )
            }
            VerticalLine(top = false, filledBottom, visibleBottom)
        }

        Icon(
            modifier = Modifier
                .size(32.dp),
            imageVector = icon,
            contentDescription = null,
            tint = Color.LightGray
        )
        Spacer(Modifier.width(10.dp))
        Column {
            Row {
                Text(
                    text = title,
                    style = Typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black.copy(alpha = 0.6f)
                )

                if (!date.isNullOrEmpty()) {
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = date,
                        style = Typography.titleMedium,
                        color = Color.Black.copy(alpha = 0.6f)
                    )
                }
            }

            Text(
                text = subtitle,
                style = Typography.labelMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
private fun VerticalLine(
    top: Boolean,
    filled: Boolean,
    visible: Boolean,
    height: Dp = 32.dp
) {
    if (filled && visible) {
        Spacer(Modifier
            .height(height)
            .width(3.dp)
            .background(MaterialTheme.colorScheme.primaryContainer)
        )
    } else if (visible) {
        VerticalDashLine(
            top = top,
            modifier = Modifier
            .height(height)
            .width(2.dp)
        )
    } else {
        Spacer(Modifier.height(height))
    }
}

@Composable
private fun VerticalDashLine(
    top: Boolean,
    modifier: Modifier = Modifier
        .height(26.dp)
        .width(2.dp)
) {
    val line = 4.dp.dpToPx()
    val offset = line.div(2)
    val pathEffect = PathEffect
        .dashPathEffect(
            intervals = floatArrayOf(line, line),
            phase = 0.dp.dpToPx()
        )

    Canvas(
        modifier = modifier
    ) {
        drawLine(
            color = Color.Gray,
            start = Offset(size.width.div(2), offset.takeIf { top }?: size.height.minus(offset)),
            end = Offset(size.width.div(2), size.height.takeIf { top }?: 0f),
            pathEffect = pathEffect,
            strokeWidth = size.width
        )
    }
}

@Composable
fun DeliveryAddress(
    address: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray.copy(alpha = 0.2f))
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Home,
                contentDescription = null
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    UiText.StringRes(track_delivery_address_title).asString(),
                    style = Typography.titleMedium,
                    color = Color.Black.copy(alpha = 0.6f)
                )
                Text(
                    UiText.StringRes(track_home_address_title).asString(),
                    style = Typography.labelMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black.copy(alpha = 0.5f)
                )
                Spacer(Modifier.height(4.dp))
                Text(address)
            }
        }
    }
}
