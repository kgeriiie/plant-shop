package com.vml.tutorial.plantshop.register.presentation.components

sealed interface RegisterEvent {
    data object RegisterClicked : RegisterEvent
    data class EmailChanged(val email: String) : RegisterEvent
    data class FirstPasswordChanged(val firstPassword: String) : RegisterEvent
    data class SecondPasswordChanged(val secondPassword: String) : RegisterEvent
    data class FirstNameChanged(val firstName: String) : RegisterEvent
    data class LastNameChanged(val lastName: String) : RegisterEvent
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
    data object NavigateBack : RegisterEvent
}
