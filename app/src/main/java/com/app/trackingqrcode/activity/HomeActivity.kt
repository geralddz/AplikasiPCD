package com.app.trackingqrcode.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.MutableLiveData
import com.app.trackingqrcode.R
import com.app.trackingqrcode.api.NotificationChannels
import com.app.trackingqrcode.api.SharedPref
import com.app.trackingqrcode.model.AndonNotifData
import com.app.trackingqrcode.socket.ListenDataAndon
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import net.mrbin99.laravelechoandroid.Echo
import net.mrbin99.laravelechoandroid.EchoOptions

class HomeActivity : AppCompatActivity() {
    private var _receivedEvent = MutableLiveData<Any>()
    private var receivedEvent = _receivedEvent
    private var echo: Echo? = null
    private lateinit var pendingIntent:PendingIntent
    private val CHANNEL_MESSAGES = "Andon"
    private lateinit var iduser: String
    private lateinit var station: String
    private lateinit var departemendId: String
    private lateinit var sharedPref: SharedPref
    private lateinit var name: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPref = SharedPref(this)
        name = sharedPref.getName().toString()
        iduser = sharedPref.getIdUser().toString()
        Log.e("iduser", "data: $iduser")
        departemendId = sharedPref.getDepartement().toString()
        Log.e("departemen_id", departemendId)
        tvUser.text = name

        connectToSocket()
        initLiveDataListener()

        live.setOnClickListener {
            startActivity(Intent(this, LiveMonitoringActivity::class.java))
        }
        history.setOnClickListener {
            startActivity(Intent(this, SummaryActivity::class.java))
        }

        scan.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }

        andon.setOnClickListener {
            startActivity(Intent(this, AndonActivity::class.java))
        }

        btnsignout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Caution !!")
                .setMessage("Apakah Anda Yakin Ingin Keluar ? ")
                .setIcon(R.drawable.logo)
                .setPositiveButton("Yes") { _: DialogInterface?, _: Int ->
                    sharedPref.isSignOut()
                    Toast.makeText(this, "Sign Out Berhasil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                }.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                }.show()

        }
    }

    private fun connectToSocket() {
        val options = EchoOptions()
        options.host = DetailPlanActivity.SERVER_URL
        options.eventNamespace = ""
        echo = Echo(options)
        echo?.connect({
            Log.d("socket", "successful connect")
            listenForEvents()
        }
        ) { args -> Log.e("socket", "error while connecting: $args") }
    }

    private fun displayNewEvent(event: ListenDataAndon?) {
        _receivedEvent.postValue(event)
    }

    private fun listenForEvents() {
        echo?.let { it ->
            it.channel(CHANNEL_MESSAGES)
                .listen("Andon_$iduser") {
                    val data = ListenDataAndon.showandon(it)
                    displayNewEvent(data)
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initLiveDataListener() {
        receivedEvent.observe(this) {
            if (it != null) {
                displayEventData(it)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "UnspecifiedImmutableFlag")
    private fun displayEventData(event: Any) {
        if (event is ListenDataAndon) {
            val temp = event.downtime[0]
            val temp1 = Gson().toJson(temp)
            val andon = Gson().fromJson(temp1, AndonNotifData::class.java)
            Log.e("new event", "data: $andon")
            station = andon.stationName.toString()

            pendingIntent = Intent(this, AndonActivity::class.java).let { notificationIntent ->
                    PendingIntent.getActivity(this, 1, notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE)
                }

            val builder = NotificationCompat.Builder(application, NotificationChannels.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Downtime Notification")
                .setContentText("Telah Terjadi Downtime Di Station $station")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent)
            val notification = builder.build()
            val notificationManager = NotificationManagerCompat.from(application)
            notificationManager.notify(1, notification)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val exitIntent = Intent(Intent.ACTION_MAIN)
        exitIntent.addCategory(Intent.CATEGORY_HOME)
        exitIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(exitIntent)
    }
}