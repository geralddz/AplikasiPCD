package com.app.trackingqrcode.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.app.trackingqrcode.R
import com.app.trackingqrcode.activity.AndonActivity
import com.app.trackingqrcode.socket.AndonSocket

class BroadcastReceiverNotif : BroadcastReceiver() {
    companion object {
        const val stationAndon = "station"
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                AndonSocket.CHANNEL_1ID, "Channel One",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.description = "Channel Satu"
            val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel1)

            val intentAndon = Intent(context, AndonActivity::class.java)
            intentAndon.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val pendingintent = PendingIntent.getActivity(context, 1, intentAndon, PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(context, AndonSocket.CHANNEL_1ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Downtime Notification")
                .setContentText("Telah Terjadi Downtime Di Station ${intent?.getStringExtra(stationAndon)}")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setVibrate(longArrayOf(0, 500, 0, 1000, 0, 2000, 0, 3000))
                .setAutoCancel(true)
                .setContentIntent(pendingintent)
            val notification = builder.build()
            manager.notify(1, notification)
        }
    }

}