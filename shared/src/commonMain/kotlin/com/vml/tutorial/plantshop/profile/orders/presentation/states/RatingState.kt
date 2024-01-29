package com.vml.tutorial.plantshop.profile.orders.presentation.states

import com.vml.tutorial.plantshop.rate.domain.Rating

data class RatingState(
    val orderId: String? = null,
    val previousRating: Rating? = null
)