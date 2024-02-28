package com.vml.tutorial.plantshop.profilePreferences.presentation.paymentMethod.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.MR.images.ic_chip
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.presentation.asString
import com.vml.tutorial.plantshop.profilePreferences.presentation.TitleSection
import com.vml.tutorial.plantshop.profilePreferences.presentation.paymentMethod.PaymentMethodScreenState
import com.vml.tutorial.plantshop.profilePreferences.presentation.paymentMethod.utils.addSlashToExpDate
import com.vml.tutorial.plantshop.profilePreferences.presentation.paymentMethod.utils.determineCardType
import com.vml.tutorial.plantshop.ui.theme.Typography
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun PaymentMethodScreen(state: PaymentMethodScreenState, onEvent: (PaymentMethodEvent) -> Unit) {
    Column {
        ToolbarSection { onEvent(PaymentMethodEvent.NavigateBack) }
        PaymentMethodContent(
            state = state, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), onEvent = onEvent
        )
    }
}

@Composable
private fun ToolbarSection(modifier: Modifier = Modifier, onNavigateBackClicked: () -> Unit) {
    IconButton(
        modifier = modifier.padding(8.dp).size(50.dp), onClick = onNavigateBackClicked
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun PaymentMethodContent(
    state: PaymentMethodScreenState, modifier: Modifier = Modifier, onEvent: (PaymentMethodEvent) -> Unit
) {
    LazyColumn(modifier = modifier) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                TitleSection(
                    UiText.StringRes(MR.strings.payment_method_title_text).asString(),
                    UiText.StringRes(MR.strings.payment_method_subtitle_text).asString(),
                    Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))) {
                    CreditCardView(state, onEvent)
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
                    onEvent(PaymentMethodEvent.SaveClicked)
                }) {
                    Text(UiText.StringRes(MR.strings.save_button_text).asString())
                }

                if (state.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CreditCardView(state: PaymentMethodScreenState, onEvent: (PaymentMethodEvent) -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer).padding(16.dp)
    ) {
        Row {
            Spacer(modifier = Modifier.weight(1F))
            IconButton(onClick = {
                onEvent(PaymentMethodEvent.CCInfoVisibilityToggled)
            }) {
                val icon = if (state.ccInfoVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                val description = if (state.ccInfoVisible) {
                    MR.strings.cc_hide_info_description
                } else {
                    MR.strings.cc_show_info_description
                }
                Icon(imageVector = icon, contentDescription = UiText.StringRes(description).asString())
            }
        }

        Image(
            painter = painterResource(ic_chip),
            contentDescription = null,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        TopSecretBasicUserInput(
            value = state.creditCardNumber.orEmpty(),
            isValueVisible = state.ccInfoVisible,
            placeholderText = UiText.StringRes(MR.strings.cc_number_placeholder_text).asString(),
            onValueChange = { onEvent(PaymentMethodEvent.OnCCNumberChanged(it.take(CC_NUMBER_DIGIT_COUNT))) },
            modifier = Modifier.fillMaxWidth()
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BasicUserInput(
                        value = state.expirationDate.orEmpty(),
                        placeholderText = UiText.StringRes(MR.strings.exp_date_placeholder_text).asString(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(108.dp)
                    ) {
                        onEvent(
                            PaymentMethodEvent.OnExpDateChanged(
                                addSlashToExpDate(it.take(EXPIRY_DATE_CHARACTER_COUNT))
                            )
                        )
                    }

                    TopSecretBasicUserInput(
                        value = state.cvv.orEmpty(),
                        isValueVisible = state.ccInfoVisible,
                        placeholderText = UiText.StringRes(MR.strings.cvv_placeholder_text).asString(),
                        onValueChange = { onEvent(PaymentMethodEvent.OnCVVChanged(it.take(CVV_DIGIT_COUNT))) },
                        modifier = Modifier.width(108.dp)
                    )
                }

                BasicUserInput(
                    value = state.cardHolderName.orEmpty(),
                    placeholderText = UiText.StringRes(MR.strings.card_holder_name_placeholder_text)
                        .asString(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.width(224.dp)
                ) {
                    onEvent(PaymentMethodEvent.OnCardHolderNameChanged(it))
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            state.creditCardNumber.takeIf { !it.isNullOrEmpty() }?.let {
                determineCardType(it)?.let { cardType ->
                    Image(
                        painter = painterResource(cardType.imageResource),
                        contentDescription = null,
                        modifier = Modifier.weight(1F).height(64.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BasicUserInput(
    value: String,
    placeholderText: String,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    BasicTextField(value = value,
        onValueChange = { newText -> onValueChange(newText) },
        keyboardOptions = keyboardOptions,
        modifier = modifier.border(
            width = 0.5.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = TEXT_OPACITY),
            RoundedCornerShape(4.dp)
        ).padding(4.dp),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
        decorationBox = { innerTextField ->
            Box {
                if (value.isEmpty()) Text(
                    placeholderText, style = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = TEXT_OPACITY)
                    )
                )
                innerTextField()
            }
        })
}

@Composable
private fun TopSecretBasicUserInput(
    value: String,
    placeholderText: String,
    isValueVisible: Boolean,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    BasicTextField(value = value,
        onValueChange = { newText -> onValueChange(newText) },
        visualTransformation = if (isValueVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        modifier = modifier.border(
            width = 0.5.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = TEXT_OPACITY),
            RoundedCornerShape(4.dp)
        ).padding(4.dp),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        decorationBox = { innerTextField ->
            Box {
                if (value.isEmpty()) Text(
                    placeholderText, style = LocalTextStyle.current.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = TEXT_OPACITY)
                    )
                )
                innerTextField()
            }
        })
}

private const val TEXT_OPACITY = 0.3F
private const val CC_NUMBER_DIGIT_COUNT = 16
private const val CVV_DIGIT_COUNT = 3
private const val EXPIRY_DATE_CHARACTER_COUNT = 5
