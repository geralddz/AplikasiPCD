package com.app.trackingqrcode.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.app.trackingqrcode.R
import com.app.trackingqrcode.adapter.PagerAdapter
import com.app.trackingqrcode.api.SharedPref
import com.app.trackingqrcode.api.SharedPrefTimer
import com.app.trackingqrcode.databinding.ActivityDetailStationBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_detail_station.*
import kotlinx.coroutines.NonCancellable.start
import java.util.*
import kotlin.math.min

class DetailStationActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPref
    private lateinit var sharedPrefTimer: SharedPrefTimer
    private lateinit var id_plan: String
    private lateinit var status: String
    private lateinit var stationname: String
    private lateinit var partname: String
    private var Tabtitle = arrayOf("shift 1", "shift 3")
    private val timer = Timer()

    companion object {
        var id_station: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_station)
        sharedPref = SharedPref(this)
        sharedPrefTimer = SharedPrefTimer(this)



        if (sharedPrefTimer.TimeCounting()) {
            startTimer()
        } else {
            stopTimer()
            if (sharedPrefTimer.startTimee() != null && sharedPrefTimer.stopTime() != null) {
                val time = Date().time - calcRestartTime().time
                timerr.text = timeStringFromLong(time)
            }
        }
        timer.scheduleAtFixedRate(TimeTask(), 0, 500)

        back.setOnClickListener {
            startActivity(Intent(this, LiveMonitoringActivity::class.java))
        }
        id_station = sharedPref.getIdStation()
        id_plan = sharedPref.getIdPlan().toString()
        status = sharedPref.getStatus().toString()
        stationname = sharedPref.getStation().toString()
        partname = sharedPref.getPartname().toString()
        Log.e("station", "onResponse: $id_station")

        namaStation.text = stationname
        statusStation.text = status

        when (status) {
            "stop" -> {
                problem.visibility = View.GONE
                onprogress.visibility = View.GONE
                stopped.visibility = View.VISIBLE
                start.visibility = View.GONE
                reset.visibility = View.GONE
            }

            "problem" -> {
                problem.visibility = View.VISIBLE
                onprogress.visibility = View.GONE
                stopped.visibility = View.GONE
                dtcty.text = partname
                start.visibility = View.GONE
                reset.visibility = View.GONE
                startAction()
            }
            else -> {
                problem.visibility = View.GONE
                onprogress.visibility = View.VISIBLE
                stopped.visibility = View.GONE
                start.visibility = View.GONE
                reset.visibility = View.GONE
                station_on.text = stationname
                part_on.text = partname
            }
        }
        viewPager.adapter = PagerAdapter(supportFragmentManager, lifecycle)
        TabLayoutMediator(tab_layout, viewPager) { tab, position ->
            tab.text = Tabtitle[position]

        }.attach()
    }

    private inner class TimeTask : TimerTask() {
        override fun run() {
            if (sharedPrefTimer.TimeCounting()) {
                val time = Date().time - sharedPrefTimer.startTimee()!!.time
                timerr.text = timeStringFromLong(time)
            }
        }
    }

    private fun startAction() {
        if (sharedPrefTimer.TimeCounting()) {
            sharedPrefTimer.setStopTime(Date())
        } else {
            if (sharedPrefTimer.stopTime() != null) {
                sharedPrefTimer.setStartTime(calcRestartTime())
                sharedPrefTimer.setStopTime(null)
            } else {
                sharedPrefTimer.setStartTime(Date())
            }
            startTimer()
        }
    }

    private fun timeStringFromLong(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60) % 60)
        val hours = (ms / (1000 * 60 * 60) % 24)
        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun stopTimer() {
        sharedPrefTimer.setTimerCounting(false)

    }

    private fun startTimer() {
        sharedPrefTimer.setTimerCounting(true)
    }

    private fun calcRestartTime(): Date {
        val diff = sharedPrefTimer.startTimee!!.time - sharedPrefTimer.stopTime()!!.time
        return Date(System.currentTimeMillis() + diff)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, LiveMonitoringActivity::class.java))
    }
}