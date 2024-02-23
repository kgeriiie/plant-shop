package com.vml.tutorial.plantshop.profilePreferences.presentation.preferences.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.presentation.AccountConfirmationDialog
import com.vml.tutorial.plantshop.core.presentation.DefaultProgressDialog
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.profilePreferences.presentation.ActionItem
import com.vml.tutorial.plantshop.profilePreferences.presentation.ToolbarSection
import com.vml.tutorial.plantshop.profilePreferences.presentation.preferences.PreferencesState

@Composable
fun PreferencesScreen(state: PreferencesState, onEvent: (PreferencesEvent) -> Unit) {
    if (state.showConfirmationDialog) {
        AccountConfirmationDialog(
            title = UiText.StringRes(MR.strings.delete_account_dialog_title).asString(),
            message = UiText.StringRes(MR.strings.delete_account_dialog_message).asString(),
            primaryPlaceholderText = UiText.StringRes(MR.strings.email_placeholder_text).asString(),
            secondaryPlaceholderText = UiText.StringRes(MR.strings.password_placeholder_text).asString(),
            onConfirmCallback = { email, password ->
                onEvent(PreferencesEvent.OnDeleteUserConfirmed(email, password))
            },
            onDismissRequest = { onEvent(PreferencesEvent.OnDeleteUserDialogDismissed) })
    }
    if (state.showProgressDialog) {
        DefaultProgressDialog(
            UiText.StringRes(MR.strings.delete_account_progress_dialog_title).asString()
        )
    }

    Column(
        modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(modifier = Modifier.size(50.dp), onClick = {
            onEvent(PreferencesEvent.NavigateBack)
        }) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        ToolbarSection(
            UiText.StringRes(MR.strings.preferences_title_text).asString(),
            UiText.StringRes(MR.strings.preferences_subtitle_text).asString(),
            Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        UserActions(modifier = Modifier.padding(horizontal = 16.dp)) { onEvent(it) }
    }
}

@Composable
private fun UserActions(modifier: Modifier = Modifier, onEvent: (PreferencesEvent) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp), modifier = modifier
    ) {
        ActionItem(iconResource = Icons.Default.House,
            text = UiText.StringRes(MR.strings.edit_address).asString(),
            onClicked = { onEvent(PreferencesEvent.OnEditAddressClicked) })
        ActionItem(iconResource = Icons.Default.Person,
            text = UiText.StringRes(MR.strings.edit_personal_info).asString(),
            onClicked = { onEvent(PreferencesEvent.OnEditPersonalDataClicked) })
        ActionItem(iconResource = Icons.Default.Payment,
            text = UiText.StringRes(MR.strings.payment_method).asString(),
            onClicked = { onEvent(PreferencesEvent.OnPaymentMethodClicked) })
        ActionItem(iconResource = Icons.Default.Delete,
            text = UiText.StringRes(MR.strings.delete_account).asString(),
            onClicked = { onEvent(PreferencesEvent.OnDeleteUserClicked) })
    }
}
