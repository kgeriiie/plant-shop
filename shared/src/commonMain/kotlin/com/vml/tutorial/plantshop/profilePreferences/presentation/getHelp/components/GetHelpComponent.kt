package com.vml.tutorial.plantshop.profilePreferences.presentation.getHelp.components

import com.arkivanov.decompose.ComponentContext
import com.vml.tutorial.plantshop.MR
import com.vml.tutorial.plantshop.core.data.config.ConfigRepository
import com.vml.tutorial.plantshop.core.presentation.UiText
import com.vml.tutorial.plantshop.core.utils.DialerUtils
import com.vml.tutorial.plantshop.core.utils.componentCoroutineScope
import com.vml.tutorial.plantshop.profilePreferences.presentation.getHelp.components.GetHelpComponentConstants.KEY_CUSTOMER_SUPPORT_NUMBER
import kotlinx.coroutines.launch

class GetHelpComponent(
    componentContext: ComponentContext,
    private val dialerUtils: DialerUtils,
    private val configRepository: ConfigRepository,
    private val onShowMessage: (message: UiText) -> Unit,
    private val onNavigateBack: () -> Unit
) : ComponentContext by componentContext {
    fun onEvent(event: GetHelpEvent) {
        when (event) {
            GetHelpEvent.CallClicked -> {
                componentCoroutineScope().launch {
                    configRepository.getConfig(KEY_CUSTOMER_SUPPORT_NUMBER)
                        ?.let { dialerUtils.dialNumber(it) } ?: run {
                            onShowMessage(UiText.StringRes(MR.strings.error_text))
                        }
                }
            }
            GetHelpEvent.NavigateBack -> onNavigateBack()
        }
    }
}

object GetHelpComponentConstants {
    const val KEY_CUSTOMER_SUPPORT_NUMBER = "customerSupportPhoneNumber"
}
