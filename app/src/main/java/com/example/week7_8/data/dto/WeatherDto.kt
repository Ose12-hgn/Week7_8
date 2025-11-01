package com.example.week7_8.data.dto

import com.google.gson.annotations.SerializedName

// Ini adalah DTO (Data Transfer Object)
// Strukturnya SAMA PERSIS dengan JSON dari API
data class WeatherResponse(
    val weather: List<WeatherDescription>,
    val main: MainStats,
    val wind: WindStats,
    val sys: SysInfo,
    val name: String,
    val clouds: CloudStats,
    val rain: RainStats?
)

data class WeatherDescription(
    val main: String
)

data class MainStats(
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int
)

data class WindStats(
    val speed: Double
)

data class SysInfo(
    val sunrise: Long,
    val sunset: Long
)

data class CloudStats(
    val all: Int
)

data class RainStats(
    @SerializedName("1h")
    val lastHour: Double?
)
