package com.rudy.weatherapp.Room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rudy.weatherapp.model.saved

@Dao
interface CityDao {

    @Query("SELECT * FROM saved")
    fun getAllCity() : LiveData<List<saved>>

    @Insert
    suspend fun addCity(city: saved)

    @Query("DELETE FROM saved WHERE id = :id")
    fun deleteCity(id: Int)

}