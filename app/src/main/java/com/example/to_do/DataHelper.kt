package com.example.to_do

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

/**
 * DataHelper class manages the start and stop times for a timer
 * using SharedPreferences to persist the data.
 */
class DataHelper (context: Context){

    // Initialize SharedPreferences to store and retrieve data
    private var sharedPref : SharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    // Date format used to store and retrieve Date objects as Strings
    private var dateFormat = SimpleDateFormat("MM/dd/yyy HH:mm:ss", Locale.getDefault())

    private var timerCounting = false;
    private var startTime: Date? = null
    private var stopTime: Date? = null

    init {
        timerCounting = sharedPref.getBoolean(COUNTING_KEY, false)

        val startString  = sharedPref.getString(START_TIME_KEY, null)
        if(startString != null)
            startTime = dateFormat.parse(startString)


        val stopString  = sharedPref.getString(STOP_TIME_KEY, null)
        if(stopString != null)
            stopTime = dateFormat.parse(stopString)
    }


    /**
     * Returns the stored start time of the timer.
     */
    fun startTime(): Date? = startTime

    fun setStartTime(date: Date?){
        startTime = date
        with(sharedPref.edit()){
            val stringDate = if(date == null) null else dateFormat.format(date)
            putString(START_TIME_KEY, stringDate)
            apply()
        }
    }


    /**
     * Returns the stored stop time of the timer.
     */
    fun stopTime(): Date? = stopTime

    fun setStoptime(date: Date?){
        stopTime = date
        with(sharedPref.edit()){
            val stringDate = if(date == null) null else dateFormat.format(date)
            putString(STOP_TIME_KEY, stringDate)
            apply()
        }
    }


    /**
     * Returns whether the timer is currently running (true) or not (false).
     */
    fun timerCounting(): Boolean = timerCounting

    fun setTimerCounting(value: Boolean){
        timerCounting = value
        with(sharedPref.edit()){
            putBoolean(COUNTING_KEY, value)
            apply()
        }
    }


    // Companion object holds constant keys used in SharedPreferences
    companion object{

        const val PREFERENCES = "prefs" // Name of the SharedPreferences file
        const val START_TIME_KEY = "startkey" // Key for storing the start time
        const val STOP_TIME_KEY = "stopkey"  // Key for storing the stop time
        const val COUNTING_KEY = "countingkey"  // Key for storing whether the timer is counting
    }

}