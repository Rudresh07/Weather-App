package com.rudy.weatherapp.Room

import android.app.Application
import androidx.room.Room

class MainApplication : Application() {

    companion object{
        lateinit var savedCityDatabase: CityDatabase
    }

    override fun onCreate() {
        super.onCreate()
        savedCityDatabase = Room.databaseBuilder(
            applicationContext,
            CityDatabase::class.java,
            CityDatabase.NAME
        ).build()
    }
}