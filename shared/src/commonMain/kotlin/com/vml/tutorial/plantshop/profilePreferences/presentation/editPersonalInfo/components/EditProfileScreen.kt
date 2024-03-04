package com.vml.tutorial.plantshop.profilePreferences.presentation.editPersonalInfo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.presentation.BirthdayDialog
import com.vml.tutorial.plantshop.core.presentation.LoadingButton
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.profilePreferences.presentation.TitleSection
import com.vml.tutorial.plantshop.core.presentation.UserInput
import com.vml.tutorial.plantshop.profilePreferences.presentation.editPersonalInfo.EditProfileScreenState
import com.vml.tutorial.plantshop.ui.theme.Typography

@Composable
fun EditProfileScreen(state: EditProfileScreenState, onEvent: (EditProfileEvent) -> Unit) {
    if (state.showDatePickerDialog) {
        BirthdayDialog(initialSelectedDateMillis = state.birthDate,
            onDateConfirm = { onEvent(EditProfileEvent.DateConfirmed(it)) },
            onDismissRequest = { onEvent(EditProfileEvent.DismissBirthdayDialog) })
    }

    LazyColumn {
        item {
            IconButton(modifier = Modifier.padding(8.dp).size(50.dp), onClick = {
                onEvent(EditProfileEvent.NavigateBack)
            }) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                TitleSection(
                    UiText.StringRes(MR.strings.edit_profile_title_text).asString(),
                    UiText.StringRes(MR.strings.edit_profile_subtitle_text).asString(),
                    Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                UserInput(
                    value = state.firstName,
                    placeholderText = UiText.StringRes(MR.strings.edit_profile_name_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                    )
                ) {
                    onEvent(EditProfileEvent.FirstNameChanged(it))
                }

                UserInput(
                    value = state.lastName,
                    placeholderText = UiText.StringRes(MR.strings.edit_profile_lastName_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                    )
                ) {
                    onEvent(EditProfileEvent.LastNameChanged(it))
                }

                UserInput(
                    value = state.phoneNumber,
                    placeholderText = UiText.StringRes(MR.strings.edit_profile_phone_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next
                    )
                ) {
                    onEvent(EditProfileEvent.PhoneNumberChanged(it))
                }

                Text(text = state.birthDateText
                    ?: UiText.StringRes(MR.strings.edit_profile_birthday_placeholder_text)
                        .asString(),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.clip(RoundedCornerShape(25.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer).height(56.dp)
                        .fillMaxWidth().padding(16.dp).clickable {
                            onEvent(EditProfileEvent.ShowBirthdayDialog)
                        })

                LoadingButton(UiText.StringRes(MR.strings.save_button_text).asString(), state.loading) {
                    onEvent(EditProfileEvent.SaveClicked)
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
            }
        }
    }
}
