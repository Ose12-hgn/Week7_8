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

    suspend fun getWeatherData(city: String): Result<Weather> {
        return try {
            val responseDto = apiService.getWeather(city, WeatherAppContainer.API_KEY)
            val weatherModel = dtoToModel(responseDto)
            Result.success(weatherModel)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun dtoToModel(dto: WeatherResponse): Weather {
        val weatherCondition = dto.weather.firstOrNull()?.main ?: "Clear"

        return Weather(
            cityName = dto.name,
            temperature = "${dto.main.temp.toInt()}°C",
            feelsLike = "${dto.main.feelsLike.toInt()}°",
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
        return when (weatherCondition.lowercase(Locale.getDefault())) {
            "rain", "drizzle" -> R.drawable.rainy_panda
            "clouds" -> R.drawable.cloudy_panda
            "clear" -> R.drawable.sunny_panda
            "thunderstorm" -> R.drawable.rainy_panda
            "snow" -> R.drawable.cloudy_panda
            "mist", "smoke", "haze", "dust", "fog", "sand", "ash", "squall", "tornado" -> R.drawable.cloudy_panda
            else -> R.drawable.sunny_panda
        }
    }
}