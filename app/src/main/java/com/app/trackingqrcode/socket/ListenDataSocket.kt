package com.app.trackingqrcode.socket

import com.google.gson.Gson

class ListenDataSocket(
    val message: String
) {
    companion object {
        fun parseFrom(value: Array<Any>): ListenDataSocket? {
            val messageData = value[1] as org.json.JSONObject
            try {
                return Gson().fromJson(messageData.toString(), ListenDataSocket::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}