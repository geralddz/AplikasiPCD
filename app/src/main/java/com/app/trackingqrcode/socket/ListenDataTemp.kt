package com.app.trackingqrcode.socket

import android.util.Log

import com.google.gson.Gson

class ListenDataTemp(
    val temp_achievement: Array<Any>,
) {
    companion object {
        fun showdata(temp_achievement: Array<Any>): ListenDataTemp?{
            val messageData = temp_achievement[1] as org.json.JSONObject
            Log.e("socketPlan", "data: $messageData")
            try {
                return Gson().fromJson(messageData.toString(), ListenDataTemp::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}