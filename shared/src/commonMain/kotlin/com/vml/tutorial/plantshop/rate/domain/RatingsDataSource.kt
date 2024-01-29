package com.vml.tutorial.plantshop.rate.domain

interface RatingsDataSource {
    suspend fun getOrderRating(userId: String, orderId: String): Rating?
    suspend fun updateRating(rating: Rating)
    suspend fun insertRating(rating: Rating)
}