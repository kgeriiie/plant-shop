package com.vml.tutorial.plantshop.profilePreferences.presentation.editAddress.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.core.utils.exts.toNonZeroString
import com.vml.tutorial.plantshop.profilePreferences.presentation.ToolbarSection
import com.vml.tutorial.plantshop.profilePreferences.presentation.UserInput
import com.vml.tutorial.plantshop.profilePreferences.presentation.editAddress.EditAddressState
import com.vml.tutorial.plantshop.ui.theme.Typography

@Composable
fun EditAddressScreen(state: EditAddressState, onEvent: (EditAddressEvent) -> Unit) {
    Column {
        ToolbarSection { onEvent(EditAddressEvent.NavigateBack) }
        EditAddressScreenContent(
            state = state,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            onEvent = onEvent
        )
    }
}

@Composable
private fun ToolbarSection(modifier: Modifier = Modifier, onNavigateBackClicked: () -> Unit) {
    IconButton(
        modifier = modifier.padding(8.dp).size(50.dp),
        onClick = onNavigateBackClicked
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun EditAddressScreenContent(
    state: EditAddressState,
    modifier: Modifier = Modifier,
    onEvent: (EditAddressEvent) -> Unit
) {
    LazyColumn(modifier = modifier) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ToolbarSection(
                    UiText.StringRes(MR.strings.edit_address_title_text).asString(),
                    UiText.StringRes(MR.strings.edit_address_subtitle_text).asString(),
                    Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                UserInput(
                    value = state.streetName,
                    placeholderText = UiText.StringRes(MR.strings.edit_address_street_name_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                    )
                ) {
                    onEvent(EditAddressEvent.StreetNameChanged(it))
                }

                UserInput(
                    value = state.doorNumber?.toNonZeroString(),
                    placeholderText = UiText.StringRes(MR.strings.edit_address_door_number_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                    )
                ) {
                    if (it.isNotEmpty()) {
                        onEvent(EditAddressEvent.DoorNumberChanged(it.toInt()))
                    }
                }

                UserInput(
                    value = state.city,
                    placeholderText = UiText.StringRes(MR.strings.edit_address_city_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                    )
                ) {
                    onEvent(EditAddressEvent.CityChanged(it))
                }

                UserInput(
                    value = state.postalCode?.toNonZeroString(),
                    placeholderText = UiText.StringRes(MR.strings.edit_address_postal_code_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                    )
                ) {
                    if (it.isNotEmpty()) {
                        onEvent(EditAddressEvent.PostalCodeChanged(it.toInt()))
                    }
                }

                UserInput(
                    value = state.country,
                    placeholderText = UiText.StringRes(MR.strings.edit_address_country_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                    )
                ) {
                    onEvent(EditAddressEvent.CountryChanged(it))
                }

                UserInput(
                    value = state.additionalDescription,
                    isSingleLine = false,
                    placeholderText = UiText.StringRes(MR.strings.edit_address_additional_desc_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.height(128.dp).verticalScroll(rememberScrollState())
                ) {
                    onEvent(EditAddressEvent.AdditionalDescriptionChanged(it))
                }

                if (state.errorMessage != null) {
                    Text(
                        state.errorMessage.asString(),
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = Color.Red,
                        style = Typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                }

                Button(modifier = Modifier.fillMaxWidth().height(50.dp), onClick = {
                    onEvent(EditAddressEvent.SaveClicked)
                }) {
                    Text(
                        UiText.StringRes(MR.strings.save_button_text).asString()
                    )
                }

                if (state.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(20.dp)
                    )
                }
            }
        }
    }
}
