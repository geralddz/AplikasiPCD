package com.app.trackingqrcode.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.trackingqrcode.R
import com.app.trackingqrcode.activity.DetailPlanActivity.Companion.SERVER_URL
import com.app.trackingqrcode.adapter.AndonAdapter
import com.app.trackingqrcode.api.SharedPref
import com.app.trackingqrcode.model.AndonData
import com.app.trackingqrcode.socket.ListenDataAndon
import com.app.trackingqrcode.socket.ListenDataTemp
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_andon.*
import kotlinx.android.synthetic.main.activity_andon.back
import kotlinx.android.synthetic.main.activity_live_monitoring.*
import kotlinx.android.synthetic.main.item_andon.*
import net.mrbin99.laravelechoandroid.Echo
import net.mrbin99.laravelechoandroid.EchoOptions

class AndonActivity : AppCompatActivity() {
    private var _receivedEvent = MutableLiveData<Any>()
    private var receivedEvent = _receivedEvent
    private var echo: Echo? = null
    private val CHANNEL_MESSAGES = "Andon"
    private lateinit var iduser: String
    private lateinit var sharedPref: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_andon)
        sharedPref = SharedPref(this)
        iduser = sharedPref.getIdUser().toString()
        Log.e("iduser", "data: $iduser")


        back.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        rvandon.layoutManager = LinearLayoutManager(this)

        modelAdapter()
        connectToSocket()
        initLiveDataListener()
    }

    private fun modelAdapter() {
        val andon = listOf(
            AndonData("Station-1060-1 Mengalami Downtime ", "Penyebab : Kuras Manifold", "10:00"),
            AndonData("Station-1060-2 Mengalami Downtime ", "Penyebab : Printer Rusak", "12:00"),
            AndonData("Station-1060-3 Mengalami Downtime ", "Penyebab : Komputer Ngelag", "09:00"),
            AndonData("Station-1060-4 Mengalami Downtime ", "Penyebab : Operator Sakit", "14:00"),
            AndonData("Station-1060-5 Mengalami Downtime ", "Penyebab : LCD Rusak", "18:00"),
            AndonData("Station-1060-6 Mengalami Downtime ", "Penyebab : Keyboard Hilang", "15:00")
        )
        val andonAdapter = AndonAdapter(andon)
        rvandon.adapter=andonAdapter
    }
    private fun connectToSocket() {
        val options = EchoOptions()
        options.host = SERVER_URL
        options.eventNamespace = ""
        echo = Echo(options)
        echo?.connect({
            Log.d("socket","successful connect")
            listenForEvents()
        }
        ) { args -> Log.e("socket","error while connecting: $args") }
    }

    private fun displayNewEvent(event: ListenDataAndon?) {
        Log.e("value", "new event $event")
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

    private fun initLiveDataListener() {
        receivedEvent.observe(this) {
            if (it != null) {
                displayEventData(it)
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun displayEventData(event: Any) {
        if (event is ListenDataAndon) {
            val temp = event.downtime[0]
            val temp1 = Gson().toJson(temp)
            Log.e("new event", "data: $temp1")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
    }
}