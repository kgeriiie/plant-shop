package com.vml.tutorial.plantshop.settings.presentation.components
/*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.register.presentation.components.RegisterEvent
import com.vml.tutorial.plantshop.register.presentation.components.UserInput
import com.vml.tutorial.plantshop.ui.theme.Typography

//TODO: Implement 2 other screens, edit profile and edit address.
Distribute below to those screens
@Composable
fun SettingsScreen() {
if (state.showDatePickerDialog) {
        BirthdayDialog(onDateConfirm = { onEvent(RegisterEvent.DateConfirmed(it)) },
            onDismissRequest = { onEvent(RegisterEvent.DismissBirthdayDialog) })
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
        state = state.postalCode?.toString().orEmpty(),
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
        state = state.postalCode?.toString().orEmpty(),
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
}

val phoneNumber: String = "",
    val showDatePickerDialog: Boolean = false,
    val birthDate: String = "DD/MM/YYYY",
    val streetName: String = "",
    val doorNumber: Int? = null,
    val city: String = "",
    val postalCode: Int? = null,
    val country: String = "",
    val additionalDescription: String = "",


    is RegisterEvent.PhoneNumberChanged -> {
                _uiState.update { it.copy(phoneNumber = event.phoneNumber) }
            }

            is RegisterEvent.DateConfirmed -> {
                _uiState.update { it.copy(birthDate = formatDate(event.birthDate, DATE_FORMAT)) }
            }

            RegisterEvent.DismissBirthdayDialog -> {
                _uiState.update { it.copy(showDatePickerDialog = false) }
            }

            RegisterEvent.ShowBirthdayDialog -> {
                _uiState.update { it.copy(showDatePickerDialog = true) }
            }

            is RegisterEvent.StreetNameChanged -> {
                _uiState.update { it.copy(streetName = event.streetName) }
            }

            is RegisterEvent.DoorNumberChanged -> {
                _uiState.update { it.copy(doorNumber = event.doorNumber) }
            }

            is RegisterEvent.CityChanged -> {
                _uiState.update { it.copy(city = event.city) }
            }

            is RegisterEvent.CountryChanged -> {
                _uiState.update { it.copy(country = event.country) }
            }

            is RegisterEvent.PostalCodeChanged -> {
                _uiState.update { it.copy(postalCode = event.postalCode) }
            }

            is RegisterEvent.AdditionalDescriptionChanged -> {
                _uiState.update { it.copy(additionalDescription = event.additionalDescription) }
            }


            data class PhoneNumberChanged(val phoneNumber: String) : RegisterEvent
    data object ShowBirthdayDialog : RegisterEvent
    data class DateConfirmed(val birthDate: Long) : RegisterEvent
    data object DismissBirthdayDialog : RegisterEvent
    data class StreetNameChanged(val streetName: String) : RegisterEvent
    data class DoorNumberChanged(val doorNumber: Int) : RegisterEvent
    data class CityChanged(val city: String) : RegisterEvent
    data class PostalCodeChanged(val postalCode: Int) : RegisterEvent
    data class CountryChanged(val country: String) : RegisterEvent
    data class AdditionalDescriptionChanged(val additionalDescription: String) : RegisterEvent
*/