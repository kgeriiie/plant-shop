package com.vml.tutorial.plantshop.profile.orders.presentation.track

import com.vml.tutorial.plantshop.profile.orders.domain.OrderActionState

sealed interface TrackOrderEvents {
    data class StateDidPressed(val state: OrderActionState): TrackOrderEvents
    data class ConfirmDialogDismissed(val item: OrderActionState, val confirmed: Boolean): TrackOrderEvents
    sealed interface ComponentEvents: TrackOrderEvents {
        data object NavigateBack: ComponentEvents
    }
}