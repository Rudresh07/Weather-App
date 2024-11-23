package com.rudy.weatherapp.Sharepreference

import android.content.Context

class SharedPreferenceManager(context: Context) {
    private val preferences = context.getSharedPreferences(
        context.packageName,
        Context.MODE_PRIVATE
    )
    private val editor = preferences.edit()

    private val keyName = "name"
    var name
        get() = preferences.getString(keyName, "Delhi") ?: "Delhi"
        set(value) {
        editor.putString(keyName, value)
        editor.commit()
    }
}