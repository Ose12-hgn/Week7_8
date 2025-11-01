package com.example.week7_8.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.week7_8.data.container.WeatherAppContainer
import com.example.week7_8.ui.model.Weather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed class WeatherUiState {
    object Initial : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val data: Weather) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

class WeatherViewModel : ViewModel() {

    private val repository = WeatherAppContainer.repository

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Initial)
    val uiState = _uiState.asStateFlow()

    fun fetchWeatherData(city: String) {
        if (city.isBlank()) {
            _uiState.value = WeatherUiState.Error("Please enter a city name")
            return
        }

        _uiState.value = WeatherUiState.Loading

        viewModelScope.launch {
            val result = repository.getWeatherData(city.trim())

            result.onSuccess { weatherModel ->
                _uiState.value = WeatherUiState.Success(weatherModel)
            }.onFailure { error ->
                val errorMessage = when (error) {
                    is HttpException -> {
                        when (error.code()) {
                            404 -> "City not found. Please check the spelling."
                            401 -> "API key error"
                            else -> "HTTP Error: ${error.code()}"
                        }
                    }
                    is java.net.UnknownHostException -> "No internet connection"
                    is java.net.SocketTimeoutException -> "Connection timeout"
                    else -> "Error: ${error.localizedMessage ?: "Something went wrong"}"
                }
                _uiState.value = WeatherUiState.Error(errorMessage)
            }
        }
    }
}