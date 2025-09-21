package com.example.memestream

import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyService {
    @GET("v1/gifs/trending")
    suspend fun getTrendingGifs(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int = 25
    ): GiphyResponse
}
