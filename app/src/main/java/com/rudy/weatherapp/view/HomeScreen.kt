package com.rudy.weatherapp.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import com.rudy.weatherapp.BuildConfig
import com.rudy.weatherapp.R
import com.rudy.weatherapp.ui.component.convertSecondsToFormattedDate
import com.rudy.weatherapp.ui.theme.brownish
import com.rudy.weatherapp.viewmodel.WeatherViewModel

@Composable
fun HomeScreen(navController: NavController) {

    val viewModel:WeatherViewModel = viewModel()
    val weatherData by viewModel.weatherData.collectAsState()
    var city by remember { mutableStateOf("Delhi") }
    var searchQuery by remember { mutableStateOf("") }
    val apiKey ="38b8823eac3e7d21715a071e978790eb"
    val weatherCondition = weatherData?.weather?.firstOrNull()?.main ?: "Sunny"
    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()


    var backgroundRes by remember { mutableStateOf(R.drawable.sunny_background) }
    var animationRes by remember { mutableStateOf(R.raw.sunny) }

    var currentDate = weatherData?.dt?.let { convertSecondsToFormattedDate(it) }
    val temprature = weatherData?.main?.temp

    // Fetch default weather data for a city when the screen loads
    LaunchedEffect(Unit) {
        viewModel.fetchWeather(city, apiKey) // Default city
    }

    


    LaunchedEffect(weatherData?.weather) {
        when (weatherCondition) {
            // Cloudy and Partly Cloudy
           "Clouds","Haze", "Hazy", "Partly cloudy", "Mist", "Intervals of clouds and sunshine", "Fog", "Freezing Fog" -> {
                backgroundRes = R.drawable.cloud_background
                animationRes = R.raw.cloud
            }
            // Clear Weather
            "Clear", "Sunny", "Mostly sunny" -> {
                backgroundRes = R.drawable.sunny_background
                animationRes = R.raw.sunny
            }
            // Snowy Weather
            "Snow", "Light snow", "Heavy snow", "Ice pellets", "Snow shower", "Moderate or heavy snow showers", "Blizzard" -> {
                backgroundRes = R.drawable.snow_background
                animationRes = R.raw.snow
            }
            // Rainy Weather
           "Rain", "Light rain", "Overcast", "Heavy rain", "Patchy rain possible", "Light drizzle", "Heavy drizzle", "Rain shower", "Torrential rain shower" -> {
                backgroundRes = R.drawable.rain_background
                animationRes = R.raw.rain
            }
            // Foggy or Hazy Weather
            "Smoke", "Dust", "Volcanic ash" -> {
                backgroundRes = R.drawable.smoky
                animationRes = R.raw.cloud
            }
        }
    }


    // Main Layout
    Box(
        modifier = Modifier
            .fillMaxSize()
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

            // Foreground Content
            Column(modifier = Modifier.fillMaxSize().padding(top = 30.dp)) {
                SearchBar(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    onSearchClicked = {
                        if (searchQuery.isNotBlank()) {
                            city = searchQuery
                            searchQuery = ""
                            viewModel.fetchWeather(city,apiKey)
                        }
                    }
                )
                Location(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    weatherData?.name ?: "Delhi"
                )
                // Show error if there's any issue fetching the data
                if (errorMessage != null) {
                    ErrorMessage("City not found. Please try another city.")
                }

                // Show loader when fetching data
                if (isLoading) {
                    LoadingIndicator()
                } else {
                    WeatherDetail(weatherCondition, currentDate, temprature)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Spacer(modifier = Modifier.weight(1f)) // Push the text to the bottom
                TextButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onClick = {
                        // Navigate to the next screen, passing viewModel to it
                        if (city.isNotBlank()) {
                            navController.navigate("DetailScreen/$city")
                        }
                    }
                ) {
                    Text("See More >>")
                }


            }
        }
    }

}

@Composable
fun LoadingIndicator() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessage(message: String) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text(text = message, color = Color.Red, fontSize = 20.sp)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(value: String, onValueChange: (String) -> Unit, onSearchClicked: () -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        placeholder = { Text("Search for a city...", color = Color.Black) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon", tint = Color.Black) },
        trailingIcon = {
            IconButton(onClick = {
                onSearchClicked()
            }){
                Icon(Icons.Filled.ArrowForward, contentDescription = "Search Button", tint = Color.Black)
            }
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = brownish,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        ),
        textStyle = LocalTextStyle.current.copy(color = Color.Black),
        shape = RoundedCornerShape(24.dp),
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = { onSearchClicked() }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search)
    )
}


@Composable
fun Location(modifier: Modifier,Location:String){
    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically) {

        Text(text = Location,
            fontSize = 20.sp,
            color = Color.Black,
            fontFamily = FontFamily.SansSerif)

    }
}

@Composable
fun WeatherDetail(weatherCondition: String,date:String?,temp:Float?){

    val weatherImageRes = when (weatherCondition) {
        "Clouds","Haze","Hazy", "Partly cloudy", "Mist", "Intervals of clouds and sunshine", "Fog", "Freezing Fog" -> R.drawable.cloudy
        "Clear", "Sunny", "Mostly sunny" -> R.drawable.sunny
        "Snow", "Light snow", "Heavy snow", "Snow shower", "Blizzard" -> R.drawable.snowy
        "Light rain", "Overcast", "Heavy rain", "Patchy rain possible", "Rain" -> R.drawable.rain
        "Smoke", "Dust", "Volcanic ash" -> R.drawable.cloudy_sunny
        else -> R.drawable.sunny // Default image if condition is unknown
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = weatherCondition,
            fontSize = 20.sp,
            color = Color.Black,
            fontFamily = FontFamily.SansSerif)

        Spacer(modifier = Modifier.height(20.dp))

        Image(painter = painterResource(weatherImageRes),
            contentDescription = "Weather description",
            modifier = Modifier.size(150.dp))

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text(text = date?:"",
                fontSize = 20.sp,
                color = Color.Black,
                fontFamily = FontFamily.SansSerif)


        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "${temp?.toInt() ?: "--"}°C", fontSize = 64.sp,
            color = Color.Black,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold
        )
    }
}



