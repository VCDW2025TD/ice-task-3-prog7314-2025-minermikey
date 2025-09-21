package com.example.memestream.DataaClasses

data class MemePost(
    val userId: String,
    val imageUrl: String,
    val caption: String,
    val lat: Double,
    val lng: Double,
    val timestamp: String
)

