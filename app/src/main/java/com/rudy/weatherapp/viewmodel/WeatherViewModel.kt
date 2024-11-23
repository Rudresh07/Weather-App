package com.rudy.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudy.weatherapp.model.WeatherApi
import com.rudy.weatherapp.model.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData: StateFlow<WeatherResponse?> = _weatherData

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val weatherApi = WeatherApi.create()

    fun fetchWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            try {
                Log.d("WeatherViewModel", "Fetching weather for city: $city")
                val response = weatherApi.getWeather(city, apiKey)
                Log.d("WeatherViewModel", "API Response: $response")
                _weatherData.value = response
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather: ${e.message}")
                _errorMessage.value = "Failed to fetch weather data. Please try again."
            } finally {
                _loading.value = false
            }
        }
    }
}
