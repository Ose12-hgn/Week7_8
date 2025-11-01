package com.example.week7_8.ui.model

import androidx.annotation.DrawableRes

// Ini adalah Model
// Data class bersih yang akan digunakan oleh UI
data class Weather(
    val cityName: String,
    val temperature: String,
    val feelsLike: String,
    val condition: String,
    val humidity: String,
    val windSpeed: String,
    val pressure: String,
    val cloudiness: String,
    val rainFall: String,
    val sunriseTime: String,
    val sunsetTime: String,
    @DrawableRes val pandaImageRes: Int
)
