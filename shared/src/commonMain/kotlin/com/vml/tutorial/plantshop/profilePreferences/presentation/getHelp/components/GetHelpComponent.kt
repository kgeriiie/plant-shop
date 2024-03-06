package com.vml.tutorial.plantshop.profilePreferences.presentation.getHelp.components

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.core.utils.DialerUtils

class GetHelpComponent(
    componentContext: ComponentContext,
    private val dialerUtils: DialerUtils,
    private val onNavigateBack: () -> Unit
) : ComponentContext by componentContext {
    fun onEvent(event: GetHelpEvent) {
        when (event) {
            GetHelpEvent.CallClicked -> dialerUtils.dialNumber("05013429432")
            GetHelpEvent.NavigateBack -> onNavigateBack()
        }
    }
}
