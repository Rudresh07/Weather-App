package com.rudy.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class saved(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var city:String
)
