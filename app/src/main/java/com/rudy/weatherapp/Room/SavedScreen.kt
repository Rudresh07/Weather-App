package com.rudy.weatherapp.Room

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.observe
import androidx.navigation.NavController
import com.rudy.weatherapp.model.saved
import com.rudy.weatherapp.ui.theme.brownish
import com.rudy.weatherapp.viewmodel.SavedCityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedCityListScreen(navController: NavController) {
    val viewModel: SavedCityViewModel = viewModel()
    // Observing the LiveData city list
    val cityList by viewModel.cityList.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        // Top App Bar
        TopAppBar(
            title = { TopSavedScreen(onBackPressed = { navController.popBackStack() }) },

        )

        // LazyColumn to display the list of cities
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp), // Adds padding to the entire list
            verticalArrangement = Arrangement.spacedBy(12.dp) // Gap between items
        ) {
            items(cityList) { city ->
                SavedCityItem(
                    city = city,

                    onRemove = {
                        viewModel.deleteCity(city.id) // Delete city using ViewModel
                    },
                    onItemClick = {
                        Log.d("CityItem", "City: $city")
                    }
                )
            }
        }
    }
}

@Composable
fun TopSavedScreen(onBackPressed: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackPressed) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Saved Cities",
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.SansSerif,
            fontSize = 24.sp,
            color = Color.Black
        )
    }
}

@Composable
fun SavedCityItem(city: saved, onRemove: () -> Unit, onItemClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(80.dp) // Adjusted the height
            .background(color = brownish, shape = RoundedCornerShape(20.dp))
            .clickable(onClick = onItemClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = city.city,
                fontSize = 22.sp,
                fontFamily = FontFamily.SansSerif
            )
            IconButton(onClick = onRemove) {
                Icon(Icons.Filled.Delete, contentDescription = "Remove City")
            }
        }
    }
}
