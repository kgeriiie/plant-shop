package com.vml.tutorial.plantshop.profilePreferences.presentation.paymentMethod

import com.vml.tutorial.plantshop.MR
import dev.icerock.moko.resources.ImageResource

enum class CardType(val cardNumberPrefix: Array<Int>, val imageResource: ImageResource) {
    MASTER_CARD(arrayOf(2, 5), MR.images.ic_mastercard),
    VISA(arrayOf(4), MR.images.img_visa)
    //TODO: Add more (or not)
}
