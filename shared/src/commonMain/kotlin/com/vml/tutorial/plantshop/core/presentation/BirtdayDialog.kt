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
import kotlinx.datetime.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayDialog(
    initialSelectedDateMillis: Long?,
    onDateConfirm: (Long) -> Unit,
    onDismissRequest: () -> Unit
) {
    val eighteenYearsInMillis = 568024668000
    val initialDateMillis = (initialSelectedDateMillis.takeIf { it != 0L } ?: (Clock.System.now()
        .toEpochMilliseconds() - eighteenYearsInMillis))
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)


    DatePickerDialog(onDismissRequest = onDismissRequest, confirmButton = {
        TextButton(onClick = {
            onDateConfirm(datePickerState.selectedDateMillis.orZero())
            onDismissRequest.invoke()
        }) {
            Text(UiText.StringRes(MR.strings.dialog_confirm_text).asString())
        }
    }, dismissButton = {
        TextButton(onClick = {
            onDismissRequest.invoke()
        }) {
            Text(UiText.StringRes(MR.strings.dialog_dismiss_text).asString())
        }
    }) {
        DatePicker(state = datePickerState)
    }
}
