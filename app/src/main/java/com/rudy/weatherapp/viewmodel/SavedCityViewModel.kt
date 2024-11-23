package com.rudy.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudy.weatherapp.Room.CityDao
import com.rudy.weatherapp.Room.MainApplication
import com.rudy.weatherapp.model.saved
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedCityViewModel : ViewModel() {

    private val repository: SavedRepository

    init {
        val savedCityDao = MainApplication.savedCityDatabase.cityDao()
        repository = SavedRepository(savedCityDao)
    }

    // LiveData to observe the saved cities list
    val cityList: LiveData<List<saved>> = repository.getAllCities()

    // Method to add a city to the database
    fun addCity(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCity(saved(city = city))
        }
    }

    // Method to delete a city from the database
    fun deleteCity(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCity(id)
        }
    }


}


class SavedRepository(private val savedDao: CityDao) {

    // Method to insert a city into the database
    suspend fun insertCity(city: saved) {
        savedDao.addCity(city)
    }



    // Method to get all cities from the database
    fun getAllCities() = savedDao.getAllCity()

    // Method to delete a city from the database
    suspend fun deleteCity(id: Int) {
        savedDao.deleteCity(id)
    }
}