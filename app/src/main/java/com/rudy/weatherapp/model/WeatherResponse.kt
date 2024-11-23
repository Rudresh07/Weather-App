package com.rudy.weatherapp.model

data class WeatherResponse(
    val name: String, // City name
    val main: Main, // Main weather details
    val weather: List<Weather>, // Weather conditions
    val wind: Wind, // Wind details
    val sys: Sys, // Sunrise and sunset times
    val dt: Long, // Current time (Unix timestamp)
    val timezone: Int, // Timezone offset
    val cloud: cloud
)

data class Main(
    val temp: Float, // Temperature in Kelvin
    val humidity: Int, // Humidity percentage
    val pressure: Int // Pressure in hPa
)

data class Weather(
    val main: String, // Weather condition (e.g., Clear, Rainy)
    val description: String, // Detailed weather description
    val icon: String // Icon code for weather conditions
)
data class cloud(
    val all: Int // Cloudiness percentage
)
data class Wind(
    val speed: Float, // Wind speed in meters/second
    val deg: Int // Wind direction in degrees
)

data class Sys(
    val country: String, // Country code (e.g., IN for India)
    val sunrise: Long, // Sunrise time (Unix timestamp)
    val sunset: Long // Sunset time (Unix timestamp)
)
