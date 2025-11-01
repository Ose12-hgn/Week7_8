package com.example.week7_8.data.container

import com.example.week7_8.data.repository.WeatherRepository
import com.example.week7_8.data.service.WeatherApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Kita gunakan 'object' untuk membuatnya jadi Singleton
object WeatherAppContainer {

    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    // API Key Anda
    const val API_KEY = "f4f6a27f8fa47f4d77829fb08084d673"

    // Membuat instance Retrofit
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    // Membuat instance Service dari Retrofit
    private val retrofitService: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }

    // Membuat instance Repository, dengan Service sebagai dependency
    val repository: WeatherRepository by lazy {
        WeatherRepository(retrofitService)
    }
}
