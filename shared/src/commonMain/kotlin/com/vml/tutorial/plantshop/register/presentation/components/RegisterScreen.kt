package com.vml.tutorial.plantshop.register.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.presentation.BirthdayDialog
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.register.presentation.RegisterUiState
import com.vml.tutorial.plantshop.ui.theme.Typography

@Composable
fun RegisterScreen(state: RegisterUiState, onEvent: (event: RegisterEvent) -> Unit) {

    if (state.showDatePickerDialog) {
        BirthdayDialog(onDateConfirm = { onEvent(RegisterEvent.DateConfirmed(it)) },
            onDismissRequest = { onEvent(RegisterEvent.DismissBirthdayDialog) })
    }

    Column {
        IconButton(
            modifier = Modifier.size(50.dp),
            onClick = {
                onEvent(RegisterEvent.NavigateBack)
            }
        ) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        UserInfoSection(state) {
            onEvent(it)
        }
    }

}

@Composable
private fun UserInfoSection(state: RegisterUiState, onEvent: (event: RegisterEvent) -> Unit) {
    LazyVerticalGrid(columns = GridCells.Fixed(1)) {
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 24.dp, start = 24.dp, end = 24.dp)
            ) {
                // TITLE
                Text(
                    text = UiText.StringRes(MR.strings.register_title_text).asString(),
                    modifier = Modifier.fillMaxWidth(),
                    style = Typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )
                //USER INFO
                Text(
                    text = UiText.StringRes(MR.strings.register_subtitle_text).asString(),
                    modifier = Modifier.fillMaxWidth(),
                    style = Typography.titleMedium,
                    color = Color.Black.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                UserInput(
                    state = state.email,
                    titleText = UiText.StringRes(MR.strings.register_email_title_text).asString(),
                    placeholderText = UiText.StringRes(MR.strings.register_email_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                    )
                ) {
                    onEvent(RegisterEvent.EmailChanged(it))
                }

                UserInput(
                    state = state.firstPassword,
                    visualTransformation = PasswordVisualTransformation(),
                    titleText = UiText.StringRes(MR.strings.register_first_password_title_text)
                        .asString(),
                    placeholderText = UiText.StringRes(MR.strings.register_first_password_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                    )
                ) {
                    onEvent(RegisterEvent.FirstPasswordChanged(it))
                }

                UserInput(
                    state = state.secondPassword,
                    visualTransformation = PasswordVisualTransformation(),
                    titleText = UiText.StringRes(MR.strings.register_second_password_title_text)
                        .asString(),
                    placeholderText = UiText.StringRes(MR.strings.register_second_password_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                    )
                ) {
                    onEvent(RegisterEvent.SecondPasswordChanged(it))
                }

                UserInput(
                    state = state.firstName,
                    titleText = UiText.StringRes(MR.strings.register_name_title_text).asString(),
                    placeholderText = UiText.StringRes(MR.strings.register_name_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                    )
                ) {
                    onEvent(RegisterEvent.FirstNameChanged(it))
                }

                UserInput(
                    state = state.lastName,
                    titleText = UiText.StringRes(MR.strings.register_lastName_title_text)
                        .asString(),
                    placeholderText = UiText.StringRes(MR.strings.register_lastName_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                    )
                ) {
                    onEvent(RegisterEvent.LastNameChanged(it))
                }

                UserInput(
                    state = state.phoneNumber,
                    titleText = UiText.StringRes(MR.strings.register_phone_title_text).asString(),
                    placeholderText = UiText.StringRes(MR.strings.register_phone_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next
                    )
                ) {
                    onEvent(RegisterEvent.PhoneNumberChanged(it))
                }

                Text(UiText.StringRes(MR.strings.register_birthday_title_text).asString())
                Text(text = state.birthDate,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.clip(RoundedCornerShape(25.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer).height(56.dp)
                        .fillMaxWidth().padding(16.dp).clickable {
                            onEvent(RegisterEvent.ShowBirthdayDialog)
                        })

                Divider()
                // ADDRESS DETAILS
                Text(
                    text = UiText.StringRes(MR.strings.register_address_title_text).asString(),
                    style = Typography.titleMedium,
                    color = Color.Black.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                UserInput(
                    state = state.streetName,
                    titleText = UiText.StringRes(MR.strings.register_street_name_title_text)
                        .asString(),
                    placeholderText = UiText.StringRes(MR.strings.register_street_name_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                    )
                ) {
                    onEvent(RegisterEvent.StreetNameChanged(it))
                }

                UserInput(
                    state =  state.postalCode?.toString().orEmpty(),
                    titleText = UiText.StringRes(MR.strings.register_door_number_title_text)
                        .asString(),
                    placeholderText = UiText.StringRes(MR.strings.register_door_number_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                    )
                ) {
                    onEvent(RegisterEvent.DoorNumberChanged(it.toInt()))
                }

                UserInput(
                    state = state.city,
                    titleText = UiText.StringRes(MR.strings.register_city_title_text).asString(),
                    placeholderText = UiText.StringRes(MR.strings.register_city_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                    )
                ) {
                    onEvent(RegisterEvent.CityChanged(it))
                }

                UserInput(
                    state =  state.postalCode?.toString().orEmpty(),
                    titleText = UiText.StringRes(MR.strings.register_postal_code_title_text)
                        .asString(),
                    placeholderText = UiText.StringRes(MR.strings.register_postal_code_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                    )
                ) {
                    onEvent(RegisterEvent.PostalCodeChanged(it.toInt()))
                }

                UserInput(
                    state = state.country,
                    titleText = UiText.StringRes(MR.strings.register_country_title_text).asString(),
                    placeholderText = UiText.StringRes(MR.strings.register_country_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                    )
                ) {
                    onEvent(RegisterEvent.CountryChanged(it))
                }

                UserInput(
                    state = state.additionalDescription,
                    isSingleLine = false,
                    titleText = UiText.StringRes(MR.strings.additional_desc_title_text).asString(),
                    placeholderText = UiText.StringRes(MR.strings.additional_desc_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.height(128.dp).verticalScroll(rememberScrollState())
                ) {
                    onEvent(RegisterEvent.AdditionalDescriptionChanged(it))
                }

                if (state.errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        state.errorMessage.asString(),
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = Color.Red,
                        style = Typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(modifier = Modifier.fillMaxWidth().height(50.dp), onClick = {
                    onEvent(RegisterEvent.RegisterClicked)
                }) {
                    Text(
                        UiText.StringRes(MR.strings.register_button_text).asString()
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

@Composable
private fun UserInput(
    state: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isSingleLine: Boolean = true,
    titleText: String,
    placeholderText: String,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Text(titleText)
    TextField(
        value = state,
        label = { Text(placeholderText) },
        onValueChange = { onValueChange(it) },
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(25.dp),
        singleLine = isSingleLine,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    )
}
