package com.example.memestream.DataaClasses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memes")
data class Meme(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Room primary key

    val caption: String, // Meme caption
    val imageUrl: String, // URL or local path to the image

    val latitude: Double? = null, // Optional latitude
    val longitude: Double? = null, // Optional longitude

    val timestamp: Long, // Time meme was created (milliseconds since epoch)
    val userId: String, // ID of the user who posted the meme
    val isSynced: Boolean = false // Track if meme is synced with backend
)
