package com.app.trackingqrcode.api

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

class SharedPrefTimer (context : Context) {
    companion object
    {
        const val PREFERENCES = "prefs"
        const val START_TIME_KEY = "startKey"
        const val STOP_TIME_KEY = "stopKey"
        const val COUNTING_KEY = "countingKey"
    }

    var pref: SharedPreferences = context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE)
    var dateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault())
    var timeCounting = false
    var startTimee : Date? = null
    var stopTimee  : Date? = null

    init {
        timeCounting = pref.getBoolean(COUNTING_KEY,false)
        val startString = pref.getString(START_TIME_KEY,null)
        if (startString !=null)
            startTimee = dateFormat.parse(startString)

        val stopString = pref.getString(STOP_TIME_KEY,null)
        if (stopString !=null)
            stopTimee = dateFormat.parse(stopString)
    }

    fun startTimee():Date? = startTimee
    fun setStartTime(date: Date?)
    {
        startTimee = date
        with(pref.edit()){
            val stringDate = if (date == null) null else dateFormat.format(date)
            putString(START_TIME_KEY,stringDate)
            apply()
        }
    }

    fun stopTime():Date? = stopTimee
    fun setStopTime(date: Date?)
    {
        stopTimee = date
        with(pref.edit()){
            val stringDate = if (date == null) null else dateFormat.format(date)
            putString(STOP_TIME_KEY,stringDate)
            apply()
        }
    }

    fun TimeCounting():Boolean = timeCounting
    fun setTimerCounting(value:Boolean)
    {
        timeCounting = value
        with(pref.edit()){
            putBoolean(COUNTING_KEY,value)
            apply()
        }
    }
}