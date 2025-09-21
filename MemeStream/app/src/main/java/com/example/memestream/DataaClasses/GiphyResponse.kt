package com.example.memestream.DataaClasses

import com.example.memestream.GiphyResponse
import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Query

// this data class will be used when the
// JSON meme are sent through from the api,
// this class will be able to process it

data class GiphyResponse(
    val data: List<GifData>
)

data class GifData(
    val id: String,
    val title: String,
    val images: GifImages
)

data class GifImages(
    @Json(name = "downsized_medium") val downsizedMedium: GifImage
)

data class GifImage(
    val url: String,
    val width: String,
    val height: String
)

data class GiphyImageDetail(
    val url: String
)
