package com.example.memestream


import com.example.memestream.api.MemeApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://rendergithub-2.onrender.com/"

    val api: MemeApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(MemeApi::class.java)
    }
}
