package com.app.trackingqrcode.socket

import android.util.Log

import com.google.gson.Gson

class ListenDataSocket(
    val temp_achievement: Array<Any>,
) {
    companion object {
        fun showdata(temp_achievement: Array<Any>): ListenDataSocket?{
            val messageData = temp_achievement[1] as org.json.JSONObject
            Log.e("socket", "data: $messageData")
            try {
                return Gson().fromJson(messageData.toString(), ListenDataSocket::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}