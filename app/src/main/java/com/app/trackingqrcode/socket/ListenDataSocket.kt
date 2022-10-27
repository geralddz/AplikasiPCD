package com.app.trackingqrcode.socket

import android.util.Log

import com.google.gson.Gson

class ListenDataSocket(
    val message: String
) {
    companion object {
        fun parseFrom(value: Array<Any>): ListenDataSocket? {
            val messageData = value[1] as org.json.JSONObject
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