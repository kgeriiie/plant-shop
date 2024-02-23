package com.vml.tutorial.plantshop.basket.presentation

import com.vml.tutorial.plantshop.MR.strings.basket_fill_address_message
import com.vml.tutorial.plantshop.MR.strings.basket_fill_phone_number_message
import com.vml.tutorial.plantshop.MR.strings.basket_user_has_no_address_message
import com.vml.tutorial.plantshop.MR.strings.basket_user_has_no_phone_number_message
import com.vml.tutorial.plantshop.MR.strings.cancel_text
import com.vml.tutorial.plantshop.MR.strings.ok_text
import com.vml.tutorial.plantshop.MR.strings.yes_text
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.utils.exts.orFalse
import com.vml.tutorial.plantshop.core.utils.exts.orZero
import com.vml.tutorial.plantshop.core.utils.exts.roundTo
import com.vml.tutorial.plantshop.plants.domain.Plant

data class BasketScreenState(
    val items: List<BasketItemState>? = null,
    val discount: Double = 3.0,   // 3 USD
    val checkoutInProgress: Boolean = false,
    val error: BasketError? = null
) {
    private val itemsPrice: Double get() = items.orEmpty().sumOf { it.totalPrice }
    private val totalPrice: Double get() = itemsPrice.minus(discount).takeUnless { items.isNullOrEmpty() }.orZero()
    val discountText: String get() = "-$ ${discount.roundTo(numFractionDigits = 2)}"
    val itemsPriceText: String get() = "$ ${itemsPrice.roundTo(numFractionDigits = 2)}"
    val totalPriceText: String get() = "$ ${totalPrice.roundTo(numFractionDigits = 2)}"

    val displayEmptyMessage: Boolean get() = items?.isEmpty().orFalse()
}

data class BasketItemState(
    val plant: Plant,
    val quantity: Int
) {
    val totalPrice: Double get() = plant.price.times(quantity)
}

data class BasketScreenUiAction(
    val checkoutInProgress: Boolean = false,
    val error: BasketError? = null
)

sealed class BasketError(val errorMessage: UiText, val positiveButton: UiText, val negativeButton: UiText?) {
    data class DefaultError(
        private val message: UiText
    ): BasketError(message, UiText.StringRes(ok_text), null)
    data object AddressMissingError: BasketError(UiText.StringRes(basket_user_has_no_address_message), UiText.StringRes(basket_fill_address_message), UiText.StringRes(cancel_text))
    data object PhoneNumberMissingError: BasketError(UiText.StringRes(basket_user_has_no_phone_number_message), UiText.StringRes(basket_fill_phone_number_message), UiText.StringRes(cancel_text))
}