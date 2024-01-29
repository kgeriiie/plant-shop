package com.vml.tutorial.plantshop.basket.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.basket.presentation.BasketError
import com.vml.tutorial.plantshop.basket.presentation.BasketItemState
import com.vml.tutorial.plantshop.basket.presentation.BasketScreenState
import com.vml.tutorial.plantshop.core.presentation.DefaultDialog
import com.vml.tutorial.plantshop.core.presentation.DefaultProgressDialog
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.plants.presentation.PlantImage
import com.vml.tutorial.plantshop.plants.presentation.detail.components.QuantityController
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderHistoryEvents
import com.vml.tutorial.plantshop.ui.theme.Typography
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun BasketScreen(
    state: BasketScreenState,
    onEvent: (BasketEvent) -> Unit
) {
    if (state.checkoutInProgress) {
        DefaultProgressDialog(UiText.StringRes(MR.strings.basket_purchasing_message).asString())
    }

    state.error?.let { error ->
        DefaultDialog(
            title = UiText.StringRes(MR.strings.warning_text).asString(),
            message = error.errorMessage.asString(),
            primaryText = error.positiveButton.asString(),
            primaryCallback = {
                when(error) {
                    BasketError.AddressMissingError -> onEvent(BasketEvent.ComponentEvents.NavigateToEditAddress)
                    is BasketError.DefaultError -> onEvent(BasketEvent.DismissErrorDialog)
                    BasketError.PhoneNumberMissingError -> onEvent(BasketEvent.ComponentEvents.NavigateToEditProfile)
                }},
            secondaryText = error.negativeButton?.asString(),
            secondaryCallback = { onEvent(BasketEvent.DismissErrorDialog) },
            onDismissRequest = { onEvent(BasketEvent.DismissErrorDialog) }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            UiText.StringRes(MR.strings.basket_screen_title).asString(),
            style = Typography.headlineMedium
        )

        Spacer(modifier = Modifier.size(20.dp))
        if (state.displayEmptyMessage) {
            // EMPTY MESSAGE
            EmptyMessage {
                onEvent(BasketEvent.ComponentEvents.NavigateToHome)
            }
        } else {
            LazyColumn {
                // BASKET ITEMS
                items(state.items.orEmpty()) {item ->
                    BasketListItem(
                        item,
                        onQuantityChanged = { plantId, quantity ->
                            onEvent(BasketEvent.OnQuantityChanged(plantId, quantity))
                        }
                    )
                    Divider(
                        modifier = Modifier
                            .padding(horizontal = 4.dp, vertical = 10.dp),
                        thickness = 1.dp,
                        color = Color.Gray.copy(alpha = 0.1f)
                    )
                }
                // SUMMARY
                item {
                    Column {
                        Spacer(modifier = Modifier.height(100.dp))
                        BasketSummary(
                            state,
                            onCheckout = {
                                onEvent(BasketEvent.Checkout)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BasketListItem(
    item: BasketItemState,
    modifier: Modifier = Modifier,
    onQuantityChanged: (plantId: Int, quantity: Int) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        PlantImage(
            url = item.plant.image,
            modifier = Modifier
                .size(120.dp, 120.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Column(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .padding(start = 12.dp)
        ) {
            Text(
                item.plant.name,
                style = Typography.titleMedium,
                color = Color.Black.copy(alpha = 0.6f),
            )
            Text(
                item.plant.originalName,
                style = Typography.titleSmall,
                fontWeight =FontWeight.Normal,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
            )
            Spacer(Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    item.plant.priceText,
                    style = Typography.headlineSmall,
                    color = Color.Black.copy(alpha = 0.6f),
                )
                QuantityController(
                    value = item.quantity,
                    onQuantityChanged = { changedValue ->
                        onQuantityChanged.invoke(item.plant.id, changedValue)
                    }
                )
            }
        }
    }
}

@Composable
private fun BasketSummary(
    state: BasketScreenState,
    modifier: Modifier = Modifier,
    onCheckout: () -> Unit,
) {
    Column(
       modifier = modifier
    ) {
        SummaryListItem(
            title = UiText.StringRes(MR.strings.basket_summary_items_title).asString(),
            value = state.itemsPriceText
        )
        SummaryListItem(
            title = UiText.StringRes(MR.strings.basket_summary_discount_title).asString(),
            value = state.discountText
        )
        Divider(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 10.dp),
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.1f)
        )
        Checkout(
            totalPrice = state.totalPriceText,
            modifier = Modifier
                .fillMaxWidth(),
            onCheckout = onCheckout
        )
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun SummaryListItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(35.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            title,
            style = Typography.titleMedium,
            fontWeight =FontWeight.Normal,
            color = Color.Gray.copy(alpha = 0.6f),
        )
        Text(
            value,
            style = Typography.titleMedium,
            color = Color.Black.copy(alpha = 0.6f),
        )
    }
}

@Composable
private fun Checkout(
    totalPrice: String,
    modifier: Modifier = Modifier,
    onCheckout: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                UiText.StringRes(MR.strings.basket_summary_total_price_title).asString(),
                style = Typography.labelMedium,
                fontWeight =FontWeight.Normal,
                color = Color.Gray.copy(alpha = 0.6f),
            )
            Text(
                totalPrice,
                style = Typography.headlineSmall,
                color = Color.Black.copy(alpha = 0.6f),
            )
        }
        Button(
            modifier = Modifier.fillMaxHeight(),
            onClick = onCheckout) {
            Text(
                text = UiText.StringRes(MR.strings.basket_summary_checkout_button).asString(),
                style = Typography.titleSmall
            )
        }
    }
}

@Composable
private fun EmptyMessage(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(120.dp))
        Image(
            painter = painterResource(MR.images.bg_empty_cart),
            contentDescription = UiText.StringRes(MR.strings.background_image).asString(),
            modifier = Modifier.size(200.dp, 200.dp)
        )
        Text(
            text = UiText.StringRes(MR.strings.basket_empty_title).asString(),
            textAlign = TextAlign.Center,
            style = Typography.titleLarge,
            color = Color.Black.copy(alpha = 0.6f),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = UiText.StringRes(MR.strings.basket_empty_message).asString(),
            textAlign = TextAlign.Center,
            style = Typography.titleMedium,
            fontWeight =FontWeight.Normal,
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onClick
        ) {
            Text(
                text = UiText.StringRes(MR.strings.basket_empty_Button).asString(),
                style = Typography.titleSmall
            )
        }
    }
}