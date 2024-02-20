package com.vml.tutorial.plantshop.profilePreferences.presentation.editPersonalInfo

import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.utils.formatDate
import com.vml.tutorial.plantshop.profilePreferences.domain.User

data class EditProfileScreenState(
    val user: User? = null,
    val firstName: String? = user?.firstName,
    val lastName: String? = user?.lastName,
    val phoneNumber: String? = user?.phoneNumber,
    val showDatePickerDialog: Boolean = false,
    val birthDate: Long? = user?.birthDate,
    val loading: Boolean = false,
    val errorMessage: UiText? = null
) {
    val birthDateText = birthDate.takeIf { it != 0L }?.let { formatDate(it, DATE_FORMAT) }

    companion object {
        const val DATE_FORMAT = "dd/MM/yyyy"
    }
}
