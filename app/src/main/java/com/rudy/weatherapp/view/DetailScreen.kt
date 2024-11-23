package com.rudy.weatherapp.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rudy.weatherapp.R
import com.rudy.weatherapp.ui.component.convertSecondsToFormattedDate
import com.rudy.weatherapp.ui.component.convertSecondsToTimeWithAMPM
import com.rudy.weatherapp.ui.theme.brownish
import com.rudy.weatherapp.viewmodel.WeatherViewModel

@Composable
fun DetailScreen(navController: NavController, cityName: String) {
    val viewModel: WeatherViewModel = viewModel()
    val weatherData by viewModel.weatherData.collectAsState()
    val apiKey = "38b8823eac3e7d21715a071e978790eb"
    val weatherCondition = weatherData?.weather?.firstOrNull()?.main ?: "Sunny"
    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var backgroundRes by remember { mutableStateOf(R.drawable.sunny_background) }
    var animationRes by remember { mutableStateOf(R.raw.sunny) }

    val currentDate = weatherData?.dt?.let { convertSecondsToFormattedDate(it) }
    val temperature = weatherData?.main?.temp
    val windSpeed = weatherData?.wind?.speed
    val humidity = weatherData?.main?.humidity
    val pressure = weatherData?.main?.pressure
    val sunrise = weatherData?.sys?.sunrise
    val sunset = weatherData?.sys?.sunset
    val rain = weatherData?.cloud?.all ?: 0


    Log.d("DetailScreen", "City Name: $rain")

    LaunchedEffect(Unit) { viewModel.fetchWeather(cityName,apiKey) }

    // Update the background and animation based on weather conditions
    LaunchedEffect(weatherCondition) {
        when (weatherCondition) {
            "Haze", "Hazy", "Partly cloudy", "Mist", "Clouds", "Fog", "Freezing Fog" -> {
                backgroundRes = R.drawable.cloud_background
                animationRes = R.raw.cloud
            }
            "Clear", "Sunny", "Mostly sunny" -> {
                backgroundRes = R.drawable.sunny_background
                animationRes = R.raw.sunny
            }
            "Snow", "Light snow", "Heavy snow", "Ice pellets", "Snow shower", "Moderate or heavy snow showers", "Blizzard" -> {
                backgroundRes = R.drawable.snow_background
                animationRes = R.raw.snow
            }
            "Light rain", "Overcast", "Heavy rain", "Patchy rain possible", "Light drizzle", "Heavy drizzle", "Rain shower", "Rain" -> {
                backgroundRes = R.drawable.rain_background
                animationRes = R.raw.rain
            }
            "Smoke", "Dust", "Volcanic ash" -> {
                backgroundRes = R.drawable.smoky
                animationRes = R.raw.cloud
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        if (isLoading)
        {
            LoadingIndicator()
        }

        else {
            Image(
                painter = painterResource(id = backgroundRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(modifier = Modifier.fillMaxSize().padding(top = 12.dp)) {
                TopBar(cityName, { navController.popBackStack() })


                if (errorMessage != null) {
                    ErrorMessage(errorMessage!!)
                }

                // Show loader when fetching data
                if (isLoading) {
                    LoadingIndicator()
                } else {
                    DetailedWeather(weatherCondition, currentDate, temperature)
                    Spacer(modifier = Modifier.height(10.dp))
                    WindDetail(windSpeed, humidity, rain, sunrise, pressure, sunset)
                }
            }
        }
    }
}

@Composable
fun DetailedWeather(weatherCondition: String, currentDate: String?, temperature: Float?) {
    val weatherImageRes = when (weatherCondition) {
        "Clouds","Haze","Hazy", "Partly cloudy", "Mist", "Intervals of clouds and sunshine", "Fog", "Freezing Fog" -> R.drawable.cloudy
        "Clear", "Sunny", "Mostly sunny" -> R.drawable.sunny
        "Snow", "Light snow", "Heavy snow", "Snow shower", "Blizzard" -> R.drawable.snowy
        "Light rain", "Overcast", "Heavy rain", "Patchy rain possible", "Rain" -> R.drawable.rain
        "Smoke", "Dust", "Volcanic ash" -> R.drawable.cloudy_sunny
        else -> R.drawable.sunny // Default image if condition is unknown
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = weatherCondition,
            fontSize = 20.sp,
            color = Color.Black,
            fontFamily = FontFamily.SansSerif
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(weatherImageRes),
            contentDescription = weatherCondition,
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text(
                text = currentDate ?: "Loading...",
                fontSize = 20.sp,
                color = Color.Black,
                fontFamily = FontFamily.SansSerif
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${temperature?.toInt() ?: "--"}°C",
            fontSize = 64.sp,
            color = Color.Black,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    locationName: String, // Name of the location to display
    onBackPressed: () -> Unit // Callback for back navigation
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically // Align content vertically
    ) {
        IconButton(onClick = onBackPressed) { // Make the back icon clickable
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black // Set the icon color
            )
        }

        Spacer(modifier = Modifier.width(8.dp)) // Add spacing between the icon and text

        Text(
            text = locationName,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.SansSerif,
            fontSize = 24.sp,
            color = Color.Black // Set text color
        )
    }
}

@Composable
fun WindDetail(
    windSpeed: Float?,
    humidity: Int?,
    rain: Int?,
    sunrise: Long?,    // Add sunrise parameter
    pressure: Int?,   // Add pressure parameter
    sunset: Long?      // Add sunset parameter
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(230.dp) // Increased height to accommodate 3 more items
            .background(color = brownish, shape = RoundedCornerShape(20.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween // Align items vertically
        ) {
            // First row: Rain, Wind, Humidity
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Rain
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.rain),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "${rain ?: "--"}%",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.W400
                    )
                    Text(
                        text = "Rain",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.W400
                    )
                }

                // Wind
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.wind),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "${windSpeed?.toInt() ?: "--"} km/h",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.W400
                    )
                    Text(
                        text = "Wind",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.W400
                    )
                }

                // Humidity
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.humidity),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "${humidity ?: "--"}%",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.W400
                    )
                    Text(
                        text = "Humidity",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.W400
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Second row: Sunrise, Pressure, Sunset
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Sunrise
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.sunrise),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "${sunrise?.let { convertSecondsToTimeWithAMPM(it) } ?: "--"}",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.W400
                    )
                    Text(
                        text = "Sunrise",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.W400
                    )
                }

                // Pressure
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.pressure),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "${pressure ?: "--"} hPa",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.W400
                    )
                    Text(
                        text = "Pressure",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.W400
                    )
                }

                // Sunset
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(R.drawable.sunset),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "${sunset?.let { convertSecondsToTimeWithAMPM(it) } ?: "--"}",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.W400
                    )
                    Text(
                        text = "Sunset",
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.W400
                    )
                }
            }
        }
    }
}

