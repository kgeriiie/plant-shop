package com.vml.tutorial.plantshop.profilePreferences.presentation.editAddress

import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.profilePreferences.domain.Address

data class EditAddressState(
    val address: Address? = null,
    val loading: Boolean = false,
    val errorMessage: UiText? = null,
    val streetName: String? = address?.streetName,
    val doorNumber: Int? = address?.doorNumber,
    val city: String? = address?.city,
    val postalCode: Int? = address?.postalCode,
    val country: String? = address?.country,
    val additionalDescription: String? = address?.additionalDescription
)
