package com.example.memestream

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class FeedViewModel : ViewModel() {

    companion object {
        private const val BASE_URL = "https://api.giphy.com/"
        private const val API_KEY = "hGkRp9EE0qTfpQjOsmDNBZzxl0QRlzUz" // Replace with actual API key
    }

    private val _trendingMemes = MutableLiveData<GiphyResponse>()
    val trendingMemes: LiveData<GiphyResponse> get() = _trendingMemes

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val giphyService: GiphyService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        giphyService = retrofit.create(GiphyService::class.java)
    }

    fun fetchTrendingMemes(limit: Int = 25) {
        viewModelScope.launch {
            try {
                val response = giphyService.getTrendingGifs(API_KEY, limit)
                _trendingMemes.postValue(response)
            } catch (e: Exception) {
                _errorMessage.postValue("Error: ${e.message}")
            }
        }
    }
}
