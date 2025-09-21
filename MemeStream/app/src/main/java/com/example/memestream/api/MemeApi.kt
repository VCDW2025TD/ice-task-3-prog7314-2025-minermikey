package com.example.memestream.api

import com.example.memestream.DataaClasses.Meme
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MemeApi {

    // Fetch all memes
    @GET("memes")
    suspend fun getMemes(): List<Meme>

    // Post a new meme
    @POST("memes")
    suspend fun postMeme(@Body meme: Meme)
}
