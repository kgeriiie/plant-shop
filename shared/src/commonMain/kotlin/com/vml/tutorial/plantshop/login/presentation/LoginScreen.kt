package com.vml.tutorial.plantshop.login.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.vml.tutorial.plantshop.MR.images.ic_login
import com.vml.tutorial.plantshop.MR.strings.login_button_text
import com.vml.tutorial.plantshop.MR.strings.login_create_acc_button_text
import com.vml.tutorial.plantshop.MR.strings.login_hide_pw_content_description
import com.vml.tutorial.plantshop.MR.strings.login_icon_content_description
import com.vml.tutorial.plantshop.MR.strings.login_not_registered_text
import com.vml.tutorial.plantshop.MR.strings.login_password_placeholder_text
import com.vml.tutorial.plantshop.MR.strings.login_show_pw_content_description
import com.vml.tutorial.plantshop.MR.strings.login_subtitle_text
import com.vml.tutorial.plantshop.MR.strings.login_title_text
import com.vml.tutorial.plantshop.MR.strings.login_username_placeholder_text
import com.vml.tutorial.plantshop.core.presentation.LoadingButton
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.register.presentation.components.RegisterEvent
import com.vml.tutorial.plantshop.ui.theme.Typography
import dev.icerock.moko.resources.compose.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LoginUiState,
    onEvent: (event: LoginEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .width(300.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painterResource(ic_login),
                modifier = Modifier.size(150.dp, 150.dp),
                contentDescription = UiText.StringRes(login_icon_content_description).asString(),
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = UiText.StringRes(login_title_text).asString(),
                modifier = Modifier.fillMaxWidth(),
                style = Typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
            )
            Text(
                text = UiText.StringRes(login_subtitle_text).asString(),
                modifier = Modifier.fillMaxWidth(),
                style = Typography.titleMedium,
                color = Color.Black.copy(alpha = 0.6f),
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField( // TODO: Use UserInput instead to avoid duplication
                value = state.username,
                label = { Text(UiText.StringRes(login_username_placeholder_text).asString()) },
                onValueChange = { onEvent(LoginEvent.UsernameChanged(it)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.PersonOutline,
                        contentDescription = null
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = state.password,
                label = { Text(UiText.StringRes(login_password_placeholder_text).asString()) },
                onValueChange = { onEvent(LoginEvent.PasswordChanged(it)) },
                visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onEvent(LoginEvent.LoginClicked)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {
                        onEvent(LoginEvent.PasswordVisibilityToggled)
                    }) {
                        val icon = if (state.passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                        val description = if (state.passwordVisible) login_hide_pw_content_description else login_show_pw_content_description

                        Icon(
                            imageVector = icon,
                            contentDescription = UiText.StringRes(description).asString()
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            LoadingButton(UiText.StringRes(login_button_text).asString(), state.loading) {
                focusManager.clearFocus()
                onEvent(LoginEvent.LoginClicked)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(UiText.StringRes(login_not_registered_text).asString())
                Text(text = UiText.StringRes(login_create_acc_button_text).asString(),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        onEvent(LoginEvent.RegisterClicked)
                    })
            }

            if (state.errorMessage != null) {
                Spacer(modifier = Modifier.height(16.dp))

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
