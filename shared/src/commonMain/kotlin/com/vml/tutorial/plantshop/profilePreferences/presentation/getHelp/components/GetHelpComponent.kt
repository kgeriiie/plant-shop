package com.vml.tutorial.plantshop.profilePreferences.presentation.getHelp.components

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.core.utils.DialerUtils
import com.vml.tutorial.plantshop.profilePreferences.presentation.getHelp.components.GetHelpComponentConstants.customerSupportPhoneNumber

class GetHelpComponent(
    componentContext: ComponentContext,
    private val dialerUtils: DialerUtils,
    private val onNavigateBack: () -> Unit
) : ComponentContext by componentContext {
    fun onEvent(event: GetHelpEvent) {
        when (event) {
            GetHelpEvent.CallClicked -> dialerUtils.dialNumber(customerSupportPhoneNumber)
            GetHelpEvent.NavigateBack -> onNavigateBack()
        }
    }
}

object GetHelpComponentConstants {
    const val customerSupportPhoneNumber = "01234567890"
}
