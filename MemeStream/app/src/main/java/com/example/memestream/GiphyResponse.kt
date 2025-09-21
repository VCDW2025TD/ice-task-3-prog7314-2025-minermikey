package com.example.memestream

import com.squareup.moshi.Json

data class GiphyResponse(
    val data: List<GifData>
)

data class GifData(
    val id: String,
    val title: String,
    val images: Images
)

data class Images(
    @Json(name = "original")
    val original: OriginalImage
)

data class OriginalImage(
    val url: String
)