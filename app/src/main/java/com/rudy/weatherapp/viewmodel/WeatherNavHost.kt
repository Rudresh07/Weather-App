package com.rudy.weatherapp.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rudy.weatherapp.Room.SavedCityListScreen
import com.rudy.weatherapp.view.DetailScreen
import com.rudy.weatherapp.view.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "HomeScreen") {
        composable("HomeScreen") {
            HomeScreen(navController)
        }
        composable(
            "DetailScreen/{cityName}",
            arguments = listOf(navArgument("cityName") { type = NavType.StringType })
        ) { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString("cityName") ?: "Unknown"
            DetailScreen(navController, cityName = cityName)
        }
        composable("SavedCityListScreen") {
            SavedCityListScreen(navController)
        }
    }
}

