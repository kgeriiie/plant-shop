package com.vml.tutorial.plantshop.rate.domain

import kotlinx.serialization.Serializable

@Serializable
data class Rating(
    val cId: String = "",
    val comment: String,
    val rating: Int,
    val userId: String = "",
    val orderId: String? = null,
    val timestamp: Long = 0
)
