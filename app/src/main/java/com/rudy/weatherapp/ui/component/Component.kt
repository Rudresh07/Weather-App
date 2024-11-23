package com.rudy.weatherapp.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudy.weatherapp.R
import com.rudy.weatherapp.ui.theme.brownish
import com.rudy.weatherapp.ui.theme.purple

@Composable
fun DailyForecast(Time: String, weatherType: String, Temp: String) {
    val weatherIcon = when (weatherType.lowercase()) {
        "sunny" -> R.drawable.sunny
        "cloudy" -> R.drawable.cloudy
        "cloudy sunny" -> R.drawable.cloudy_sunny
        "rainy" -> R.drawable.rainy
        "snowy" -> R.drawable.snowy
        "windy" -> R.drawable.windy
        "storm" -> R.drawable.storm
        else -> R.drawable.sunny
    }

    Box(
        modifier = Modifier
            .width(70.dp)
            .height(100.dp)
            .background(color = brownish.copy(alpha = 0.9f), shape = RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 2.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = Time,
                fontSize = 12.sp,
                fontWeight = FontWeight.W400,
                fontFamily = FontFamily.SansSerif,
                color = Color.Black
            )
            Image(
                painter = painterResource(weatherIcon),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = Temp,
                fontSize = 12.sp,
                fontWeight = FontWeight.W400,
                fontFamily = FontFamily.SansSerif,
                color = Color.Black
            )
        }
    }
}



@Preview
@Composable
fun PreviewDailyForecast(){
    DailyForecast("12:22 pm", "Sunny","22°C")
}