package com.vml.tutorial.plantshop.profilePreferences.presentation.paymentMethod.utils

import com.vml.tutorial.plantshop.profilePreferences.presentation.paymentMethod.CardType

fun determineCardType(cardNumber: String): CardType? {
    val prefix = cardNumber.first().digitToInt()

    return CardType.entries.firstOrNull { cardType ->
        prefix in cardType.cardNumberPrefix
    }
}

fun addSlashToExpDate(input: String): String {
    val cleanedInput = input.filter { char -> char.isDigit() }
    return if (cleanedInput.length == 4) {
        "${cleanedInput.substring(0, 2)}/${cleanedInput.substring(2)}"
    } else {
        cleanedInput
    }
}
