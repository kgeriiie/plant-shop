package com.vml.tutorial.plantshop.profilePreferences.presentation.editPersonalInfo.components

sealed interface EditProfileEvent {
    data object NavigateBack : EditProfileEvent
    data class FirstNameChanged(val firstName: String) : EditProfileEvent
    data class LastNameChanged(val lastName: String) : EditProfileEvent
    data class PhoneNumberChanged(val phoneNumber: String) : EditProfileEvent
    data object ShowBirthdayDialog : EditProfileEvent
    data class DateConfirmed(val birthDate: Long) : EditProfileEvent
    data object DismissBirthdayDialog : EditProfileEvent
    data object SaveClicked : EditProfileEvent
}
