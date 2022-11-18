package com.app.trackingqrcode.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.app.trackingqrcode.R
import com.app.trackingqrcode.adapter.PagerAdapter
import com.app.trackingqrcode.api.SharedPref
import com.app.trackingqrcode.api.SharedPrefTimer
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_detail_station.*
import java.time.LocalTime
import java.util.*

class DetailStationActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPref
    private lateinit var sharedPrefTimer: SharedPrefTimer
    private lateinit var startDowntime: String
    private lateinit var status: String
    private lateinit var stationname: String
    private lateinit var partname: String
    private lateinit var downtimecty : String

    private var tabtitle = arrayOf("shift 1", "shift 3")
    private val timer = Timer()
    private var selisihdtk: Int = 0
    private var selisihmnt: Int = 0
    private var selisihjam: Int = 0
    private var selisihwaktu: Int = 0
    private lateinit var hour: String
    private lateinit var minute: String
    private lateinit var sec: String
    private lateinit var waktu: String

    companion object {
        var id_station: String? = null
        var id_plan: String? = null
    }
    @RequiresApi(Build.VERSION_CODES.O)
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
//        timer.scheduleAtFixedRate(TimeTask(), 0, 500)

        back.setOnClickListener {
            startActivity(Intent(this, LiveMonitoringActivity::class.java))
        }

        id_station = sharedPref.getIdStation()
        id_plan = sharedPref.getIdPlan().toString()
        Log.e("idplan", "onResponse: $id_plan")
        status = sharedPref.getStatus().toString()
        stationname = sharedPref.getStation().toString()
        partname = sharedPref.getPartname().toString()
        downtimecty = sharedPref.getDowntimeCategory().toString()
        startDowntime = sharedPref.getStartTime().toString()
        Log.e("station", "onResponse: $id_station")
        Log.e("time", "onResponse: $startDowntime")

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
                dtcty.text = downtimecty
                start.visibility = View.GONE
                reset.visibility = View.GONE
                getTimeNow()
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
            tab.text = tabtitle[position]

        }.attach()
    }

//    private inner class TimeTask : TimerTask() {
//        override fun run() {
//            if (sharedPrefTimer.TimeCounting()) {
//                val time = Date().time - sharedPrefTimer.startTimee()!!.time
//                timerr.text = timeStringFromLong(time)
//            }
//        }
//    }

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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTimeNow() {
        val timenow = LocalTime.now()
        val splitan = timenow.toString().split(".").toTypedArray()
        val time = splitan[0]
        val realtimesplit = time.split(":").toTypedArray()
        val downtimesplit = startDowntime.split(":").toTypedArray()
        //downtime
        var downjam = downtimesplit[0].toInt()
        var downmnt = downtimesplit[1].toInt()
        var downdtk = downtimesplit[2].toInt()
        //realtime
        val jamnow = realtimesplit[0].toInt()
        val mntnow = realtimesplit[1].toInt()
        val dtknow = realtimesplit[2].toInt()

        if (downdtk > dtknow) {
            while (downdtk != dtknow) {
                if (downdtk == 60) {
                    downdtk = 0
                    downmnt++
                    continue
                }
                downdtk++
                selisihdtk++
            }
        } else if (downdtk < dtknow) {
            selisihdtk = dtknow - downdtk
        }

        selisihwaktu = selisihdtk

        if (downmnt > mntnow) {
            while (downmnt != mntnow) {
                if (downmnt == 60) {
                    downmnt = 0
                    downjam++
                    continue
                }
                downmnt++
                selisihmnt++
            }
        } else if (downmnt < mntnow) {
            selisihmnt = mntnow - downmnt
        }

        selisihwaktu += selisihmnt * 60

        if (downjam > jamnow) {
            while (downjam != jamnow) {
                if (downjam == 24) {
                    downjam = 0
                    continue
                }
                downjam++
                selisihjam++
            }
        } else if (downjam < jamnow) {
            selisihjam = jamnow - downjam
        }

        selisihwaktu += selisihjam * 3600


        val jam: Int = selisihwaktu / 3600

        selisihwaktu %= 3600

        val menit: Int
        val detik: Int
        if (selisihwaktu >= 60) {
            menit = selisihwaktu / 60
            detik = selisihwaktu % 60
        } else {
            menit = 0
            detik = selisihwaktu
        }


        hour = if (jam < 10) {
            "0$jam"
        } else {
            "$jam"
        }

        minute = if (menit < 10) {
            "0$menit"
        } else {
            "$menit"
        }

        sec = if (detik < 10) {
            "0$detik"
        } else {
            "$detik"
        }

        waktu = "$hour:$minute:$sec"
        timerr.text = waktu
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

    private fun resetActionTimer() {
        sharedPrefTimer.setStopTime(null)
        sharedPrefTimer.setStartTime(null)
        stopTimer()
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