package com.app.trackingqrcode.socket

import android.util.Log
import com.google.gson.Gson

class ListenDataAndon (
    val downtime: Array<Any>,
) {
        companion object {
            fun showandon(downtime: Array<Any>): ListenDataAndon? {
                val messageData = downtime[1] as org.json.JSONObject
                Log.e("socket", "data: $messageData")
                try {
                    return Gson().fromJson(messageData.toString(), ListenDataAndon::class.java)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return null
            }
        }
    }