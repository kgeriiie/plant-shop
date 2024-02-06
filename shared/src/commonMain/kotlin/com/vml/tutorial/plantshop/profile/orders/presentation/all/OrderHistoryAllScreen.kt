package com.vml.tutorial.plantshop.profile.orders.presentation.all

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.presentation.DefaultDialog
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.core.utils.exts.orZero
import com.vml.tutorial.plantshop.profile.orders.presentation.OrderListItem
import com.vml.tutorial.plantshop.profile.orders.presentation.states.OrderHistoryEvents
import com.vml.tutorial.plantshop.ui.theme.Typography

@Composable
fun OrderHistoryAllScreen(
    state: OrderHistoryAllUiState,
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

    Column(
        modifier = Modifier
            .fillMaxSize(),
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
                text = state.screenTitle.asString(),
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
        LazyColumn(
            contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            items(
                state.items?.size.orZero()
            ) {index ->
                state.items?.getOrNull(index)?.let { itemState ->
                    OrderListItem(
                        itemState = itemState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    ) {
                        onEvent(it)
                    }
                }
            }
        }
    }

    LaunchedEffect("init-screen") {
        onEvent(OrderHistoryEvents.FetchContents)
    }
}