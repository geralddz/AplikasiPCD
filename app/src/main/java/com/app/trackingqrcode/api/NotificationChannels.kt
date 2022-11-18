package com.app.trackingqrcode.api

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class NotificationChannels: Application() {
    companion object {
        const val CHANNEL_1_ID = "channel1"
        const val CHANNEL_2_ID = "channel2"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                CHANNEL_1_ID, "Channel One",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "Channel Satu"

            val channel2 = NotificationChannel(
                CHANNEL_2_ID, "Channel Two",
                NotificationManager.IMPORTANCE_LOW
            )
            channel2.description = "Channel Dua"

            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel1)
            manager?.createNotificationChannel(channel2)
        }
    }
}