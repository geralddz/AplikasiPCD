package com.app.trackingqrcode.socket

import android.app.Application
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import com.app.trackingqrcode.R
import com.app.trackingqrcode.model.AndonNotifData
import com.app.trackingqrcode.utils.ApiUtils.Companion.SOCKET_URL
import com.app.trackingqrcode.utils.BroadcastReceiverNotif
import com.app.trackingqrcode.utils.SharedPref
import com.google.gson.Gson
import net.mrbin99.laravelechoandroid.Echo
import net.mrbin99.laravelechoandroid.EchoOptions
import org.json.JSONObject

class AndonSocket : Application() {
    companion object {
        const val CHANNEL_1ID = "channel1"
        const val stationAndon = "station"
    }
    private var echo: Echo? = null
    private val CHANNEL_MESSAGES = "Andon"
    private lateinit var iduser: String
    private lateinit var sharedPref: SharedPref
    var mp: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()
        sharedPref = SharedPref(this)
        mp = MediaPlayer.create(this, R.raw.andon)
        iduser = sharedPref.getIdUser().toString()
        connectToSocket()
    }

    private fun connectToSocket() {
        val options = EchoOptions()
        options.host = SOCKET_URL
        options.eventNamespace = ""
        echo = Echo(options)
        echo?.connect({
            Log.d("socket", "successful connect")
            listenForEvents()
        }
        ) { args -> Log.e("socket", "error while connecting: $args") }
    }

    private fun listenForEvents() {
        echo?.let { it ->
            it.channel(CHANNEL_MESSAGES)
                .listen("Andon_$iduser") {
                    Log.e("iduser", "data: $iduser")
                    val data = showandon(it)
                    if (data != null) {
                        displayEventData(data)
                    }
                }
        }
    }

    private fun showandon(andon: Array<Any>): JSONObject?{
        val messageData = andon[1] as JSONObject
        Log.e("socketAndon", "data: $messageData")
        try {
            return messageData
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun displayEventData(event: JSONObject) {
        val temp1 = event.optJSONArray("downtime")?.get(0).toString()
        val andon = Gson().fromJson(temp1, AndonNotifData::class.java)
        val stationandon = andon.stationName.toString()
        mp?.start()
        val intent = Intent(this, BroadcastReceiverNotif::class.java)
        intent.putExtra(stationAndon, stationandon)
        sendBroadcast(intent)
    }

}