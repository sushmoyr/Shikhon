package com.sushmoyr.shikhon.backend.data

data class Review(
    val reviewId: String,
    val rating: Float,
    val review: String,
    val reviewedUserId: String,
    val reviewBy: String
)
