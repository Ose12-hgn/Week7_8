package com.example.week7_8.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week7_8.data.container.WeatherAppContainer
import com.example.week7_8.ui.model.Weather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// UI State sekarang menyimpan Model (Weather), bukan DTO (WeatherResponse)
sealed class WeatherUiState {
    object Initial : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val data: Weather) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

class WeatherViewModel : ViewModel() {

    // Dapatkan repository dari Container, sama seperti di aplikasi film Anda
    private val repository = WeatherAppContainer.repository

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Initial)
    val uiState = _uiState.asStateFlow()

    fun fetchWeatherData(city: String) {
        if (city.isBlank()) return

        _uiState.value = WeatherUiState.Loading
        viewModelScope.launch {
            val result = repository.getWeatherData(city)

            result.onSuccess { weatherModel ->
                // Sukses! Berikan Model bersih ke UI State
                _uiState.value = WeatherUiState.Success(weatherModel)
            }.onFailure { error ->
                // Tangani error
                val errorMessage = if (error is retrofit2.HttpException && error.code() == 404) {
                    "HTTP 404 Not Found"
                } else {
                    "Oops! Something went wrong"
                }
                _uiState.value = WeatherUiState.Error(errorMessage)
            }
        }
    }
}
