package com.example.week7_8.data.repository

import com.example.week7_8.R
import com.example.week7_8.data.container.WeatherAppContainer
import com.example.week7_8.data.dto.WeatherResponse
import com.example.week7_8.data.service.WeatherApiService
import com.example.week7_8.ui.model.Weather
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherRepository(private val apiService: WeatherApiService) {

    // Fungsi ini sekarang mengembalikan Result<Weather> (Model)
    // bukan Result<WeatherResponse> (DTO)
    suspend fun getWeatherData(city: String): Result<Weather> {
        return try {
            val responseDto = apiService.getWeather(city, WeatherAppContainer.API_KEY)
            // Ubah DTO mentah menjadi Model yang bersih
            val weatherModel = dtoToModel(responseDto)
            Result.success(weatherModel)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- FUNGSI MAPPING (LOGIKA DIPINDAH KE SINI) ---

    // Ini adalah inti dari pemisahan: mengubah DTO ke Model
    private fun dtoToModel(dto: WeatherResponse): Weather {
        val weatherCondition = dto.weather.firstOrNull()?.main ?: "Clear"

        return Weather(
            cityName = dto.name,
            temperature = "${dto.main.temp.toInt()}°C",  // ✅ FIXED: Karakter derajat yang benar
            feelsLike = "${dto.main.feelsLike.toInt()}°",  // ✅ FIXED: Karakter derajat yang benar
            condition = weatherCondition,
            humidity = "${dto.main.humidity}%",
            windSpeed = "${dto.wind.speed} km/h",
            pressure = "${dto.main.pressure} hPa",
            cloudiness = "${dto.clouds.all}%",
            rainFall = "${dto.rain?.lastHour ?: 0.0} mm",
            sunriseTime = formatTime(dto.sys.sunrise),
            sunsetTime = formatTime(dto.sys.sunset),
            pandaImageRes = getPandaImage(weatherCondition)
        )
    }

    private fun formatTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }

    private fun getPandaImage(weatherCondition: String): Int {
        // ✅ FIXED: Menyesuaikan dengan nama drawable yang tersedia
        return when (weatherCondition.lowercase()) {
            "rain", "drizzle" -> R.drawable.RainyPanda// Ganti dari panda_rain
            "clouds" -> R.drawable.CloudyPanda          // Ganti dari panda_cloud
            "clear" -> R.drawable.SunnyPanda            // Ganti dari panda_clear
            else -> R.drawable.SunnyPanda
        }
    }
}