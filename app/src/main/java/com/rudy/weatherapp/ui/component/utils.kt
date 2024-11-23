package com.rudy.weatherapp.ui.component

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun convertSecondsToFormattedDate(seconds: Long): String {
    // Convert seconds to milliseconds
    val date = Date(seconds * 1000)

    // Create a SimpleDateFormat for the desired format (date + time with AM/PM)
    val format = SimpleDateFormat("EEE MMM dd | hh:mm a", Locale.getDefault())

    // Return the formatted date and time
    return format.format(date)
}



fun convertSecondsToTimeWithAMPM(seconds: Long): String {
    val date = Date(seconds * 1000)  // Convert seconds to milliseconds
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())  // Define the time format
    return sdf.format(date)
}
