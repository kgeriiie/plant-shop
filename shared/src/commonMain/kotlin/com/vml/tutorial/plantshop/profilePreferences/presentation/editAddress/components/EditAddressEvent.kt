package com.vml.tutorial.plantshop.profilePreferences.presentation.editAddress.components

sealed interface EditAddressEvent {
    data object NavigateBack: EditAddressEvent

    data class StreetNameChanged(val streetName: String) : EditAddressEvent
    data class DoorNumberChanged(val doorNumber: Int) : EditAddressEvent
    data class CityChanged(val city: String) : EditAddressEvent
    data class PostalCodeChanged(val postalCode: Int) : EditAddressEvent
    data class CountryChanged(val country: String) : EditAddressEvent
    data class AdditionalDescriptionChanged(val additionalDescription: String) : EditAddressEvent
    data object SaveClicked: EditAddressEvent
}
