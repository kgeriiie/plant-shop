package com.vml.tutorial.plantshop.profilePreferences.presentation.paymentMethod

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.profilePreferences.data.ProfileRepository
import com.vml.tutorial.plantshop.profilePreferences.domain.PaymentMethod
import com.vml.tutorial.plantshop.profilePreferences.domain.User
import com.vml.tutorial.plantshop.profilePreferences.presentation.paymentMethod.components.PaymentMethodEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaymentMethodComponent(
    componentContext: ComponentContext,
    private val user: User?,
    private val profileRepository: ProfileRepository,
    private val onShowMessage: (message: UiText) -> Unit,
    private val onNavigateBack: () -> Unit
) :
    ComponentContext by componentContext {
    private val _state = MutableStateFlow(PaymentMethodScreenState(paymentMethod = user?.paymentMethod))
    val state: StateFlow<PaymentMethodScreenState> = _state.asStateFlow()

    fun onEvent(event: PaymentMethodEvent) {
        when (event) {
            PaymentMethodEvent.NavigateBack -> onNavigateBack()
            PaymentMethodEvent.SaveClicked -> {
                if (checkValidity()) {
                    user.apply { this?.paymentMethod = getPaymentMethod(state.value) }
                        ?.let { updatePaymentMethod(it) }
                        ?: run { _state.update { it.copy(errorMessage = UiText.StringRes(MR.strings.save_error_text)) } }
                } else {
                    _state.update { it.copy(errorMessage = UiText.StringRes(MR.strings.save_error_text)) }
                }
            }

            is PaymentMethodEvent.OnCCNumberChanged -> {
                this._state.update { it.copy(creditCardNumber = event.creditCardNumber) }
            }

            is PaymentMethodEvent.OnExpDateChanged -> {
                this._state.update { it.copy(expirationDate = event.expirationDate) }
            }

            is PaymentMethodEvent.OnCVVChanged -> {
                this._state.update { it.copy(cvv = event.cvvNumber) }
            }

            is PaymentMethodEvent.OnCardHolderNameChanged -> {
                this._state.update { it.copy(cardHolderName = event.cardHolderName) }
            }

            PaymentMethodEvent.CCInfoVisibilityToggled -> {
                this._state.update { it.copy(ccInfoVisible = !state.value.ccInfoVisible) }
            }
        }
    }

    private fun getPaymentMethod(state: PaymentMethodScreenState): PaymentMethod {
        return PaymentMethod(
            creditCardNumber = state.creditCardNumber,
            expirationDate = state.expirationDate,
            cvv = state.cvv,
            cardHolderName = state.cardHolderName
        )
    }

    private fun updatePaymentMethod(userInfo: User) {
        componentCoroutineScope().launch {
            _state.update { it.copy(loading = true) }
            if (!profileRepository.updateUserInfo(userInfo)) {
                _state.update { it.copy(errorMessage = UiText.StringRes(MR.strings.save_error_text)) }
            } else {
                _state.update { it.copy(errorMessage = null) }
                onShowMessage(UiText.StringRes(MR.strings.saved_text))
            }
            _state.update { it.copy(loading = false) }
        }
    }

    private fun checkValidity(): Boolean {
        return state.value.creditCardNumber?.length == 16 &&
                state.value.cvv?.length == 3 &&
                state.value.expirationDate?.length == 5 &&
                !state.value.cardHolderName.isNullOrEmpty()
    }
}
