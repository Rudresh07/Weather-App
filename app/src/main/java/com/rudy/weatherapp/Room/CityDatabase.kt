package com.rudy.weatherapp.Room


import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.RoomDatabase
import com.rudy.weatherapp.model.saved

@Database(entities = [saved::class], version = 1)
abstract class CityDatabase : RoomDatabase() {
    companion object{
        const val  NAME = "City_DB"
    }
    abstract fun cityDao() : CityDao

}