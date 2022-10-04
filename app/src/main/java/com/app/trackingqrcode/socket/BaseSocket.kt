package com.app.trackingqrcode.socket

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import net.mrbin99.laravelechoandroid.Echo
import net.mrbin99.laravelechoandroid.EchoOptions

const val SERVER_URL = "http://10.0.2.2:6001"
const val CHANNEL_MESSAGES = "messages"
const val EVENT_MESSAGE_CREATED = "MessageCreated"
const val TAG = "msg"

open class BaseSocket : AppCompatActivity() {
    private var _receivedEvent = MutableLiveData<Any>()
    var receivedEvent = _receivedEvent

    private var echo: Echo? = null

    fun connectToSocket() {
        val options = EchoOptions()
        options.host = SERVER_URL

        echo = Echo(options)
        echo?.connect({
            log("successful connect")
            listenForEvents()
        }
        ) { args -> log("error while connecting: $args") }
    }

    private fun listenForEvents() {
        echo?.let {
            it.channel(CHANNEL_MESSAGES)
                .listen(EVENT_MESSAGE_CREATED) {
                    val newEvent = ListenDataSocket.parseFrom(it)
                    displayNewEvent(newEvent)
                }
        }
    }

    fun disconnectFromSocket() {
        echo?.disconnect()
    }

    private fun log(message: String) {
        Log.i(TAG, message)
    }

    private fun displayNewEvent(event: ListenDataSocket?) {
        log("new event " + event?.message)
        _receivedEvent.postValue(event)
    }
}
