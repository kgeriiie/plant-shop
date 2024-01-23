package com.vml.tutorial.plantshop.core.presentation

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.utils.exts.orZero

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayDialog(onDateConfirm: (Long) -> Unit, onDismissRequest: () -> Unit) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(onDismissRequest = onDismissRequest, confirmButton = {
        TextButton(onClick = {
            onDateConfirm(datePickerState.selectedDateMillis.orZero())
            onDismissRequest.invoke()
        }) {
            Text(UiText.StringRes(MR.strings.pick_birth_date_dialog_confirm_text).asString())
        }
    }, dismissButton = {
        TextButton(onClick = {
            onDismissRequest.invoke()
        }) {
            Text(UiText.StringRes(MR.strings.pick_birth_date_dialog_dismiss_text).asString())
        }
    }) {
        DatePicker(
            state = datePickerState
        )
    }
}
