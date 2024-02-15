package com.vml.tutorial.plantshop.profilePreferences.presentation.preferences

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.profilePreferences.data.ProfileRepository
import com.vml.tutorial.plantshop.profilePreferences.domain.User
import com.vml.tutorial.plantshop.profilePreferences.presentation.preferences.components.PreferencesEvent
import kotlinx.coroutines.launch

class PreferencesComponent(
    componentContext: ComponentContext,
    private val profileRepository: ProfileRepository,
    private val onComponentEvent: (event: PreferencesEvent, user: User?) -> Unit
) : ComponentContext by componentContext {
    fun onEvent(event: PreferencesEvent) {
        when (event) {
            PreferencesEvent.OnDeleteProfileClicked -> TODO()
            PreferencesEvent.OnPaymentMethodClicked -> TODO()
            else -> {
                componentCoroutineScope().launch {
                    onComponentEvent(event, profileRepository.getUser())
                }
            }
        }
    }
}
