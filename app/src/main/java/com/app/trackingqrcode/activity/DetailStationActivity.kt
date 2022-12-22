package com.app.trackingqrcode.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.telecom.Call
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.app.trackingqrcode.R
import com.app.trackingqrcode.adapter.PagerAdapter
import com.app.trackingqrcode.response.DowntimeResponse
import com.app.trackingqrcode.response.LiveStationResponse
import com.app.trackingqrcode.utils.ApiUtils
import com.app.trackingqrcode.utils.SharedPref
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_detail_part.*
import kotlinx.android.synthetic.main.activity_detail_station.*
import kotlinx.android.synthetic.main.activity_detail_station.namaStation
import kotlinx.android.synthetic.main.activity_detail_station.statusStation
import retrofit2.Response
import java.time.LocalTime
import java.util.*
import javax.security.auth.callback.Callback

class DetailStationActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPref
    private lateinit var startDowntime: String
    private lateinit var status: String
    private lateinit var stationname: String
    private var startTime: String = ""
    private lateinit var downtime : String
    private lateinit var partname: String
    private var downtimecty : String = ""
    private var tabtitle = arrayOf("shift 1", "shift 3")
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

        back.setOnClickListener {
            startActivity(Intent(this, LiveMonitoringActivity::class.java))
        }

        id_station = sharedPref.getIdStation()
        id_plan = sharedPref.getIdPlan().toString()
        Log.e("idplan", "onResponse: $id_plan")
        status = sharedPref.getStatus().toString()
        stationname = sharedPref.getStation().toString()
        partname = sharedPref.getPartname().toString()
        downtime = sharedPref.getIsDowntime().toString()
        Log.e("station", "onResponse: $id_station")

        namaStation.text = stationname

        if (status=="Start"){
            if (downtime=="1"){
                if (id_station != "null" && id_plan != "null") {
                    showDetailStation()
                }
                problem.visibility = View.VISIBLE
                onprogress.visibility = View.GONE
                stopped.visibility = View.GONE
                downtimecty = sharedPref.getDowntimeCategory().toString()
                startTime = sharedPref.getStartTime().toString()
                if (startTime!=""){
                    counterTime()
                    dtcty.text = downtimecty
                    timerr.visibility = View.VISIBLE
                    statusPenanganan.visibility = View.VISIBLE
                }else{
                    dtcty.text = partname
                    timerr.visibility = View.GONE
                    statusPenanganan.visibility = View.GONE

                }
                statusStation.text = "Problem"
            }else{
                problem.visibility = View.GONE
                onprogress.visibility = View.VISIBLE
                stopped.visibility = View.GONE
                station_on.text = stationname
                statusStation.text = "Start"
                part_on.text = partname
            }
        }else{
            problem.visibility = View.GONE
            onprogress.visibility = View.GONE
            stopped.visibility = View.VISIBLE
            statusStation.text = "Stop"
        }

        viewPager.adapter = PagerAdapter(supportFragmentManager, lifecycle)
        TabLayoutMediator(tab_layout, viewPager) { tab, position ->
            tab.text = tabtitle[position]

        }.attach()
    }
    private fun showDetailStation(){
        val retro = ApiUtils().getUserService()
        retro.getDowntime(id_station, id_plan).enqueue(object : retrofit2.Callback<DowntimeResponse> {
                override fun onResponse(call: retrofit2.Call<DowntimeResponse>,response: Response<DowntimeResponse>) {
                    val downtime = response.body()
                    val downtimedata = downtime?.data
                    if (downtimedata != null) {
                        for (d in downtimedata.indices){
                            val downtimeCt = downtimedata[d]?.downtimeCategory
                            val startt = downtimedata[d]?.startTime.toString()
                            val splitan = startt.split(" ").toTypedArray()
                            val dtcty = downtimeCt.toString()
                            val start = splitan[1]
                            startTime = start
                            downtimecty = dtcty
                            sharedPref.setStartTime(start)
                            sharedPref.setDowntimeCategory(dtcty)
                            Log.e("starttime", startTime)
                        }
                    }else{
                        Toast.makeText(this@DetailStationActivity, "DataDowntime Tidak Ada", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<DowntimeResponse>, t: Throwable) {
                    Log.e("Error", t.message!!)
                }
            })
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun counterTime() {
        downtimecty = sharedPref.getDowntimeCategory().toString()
        startTime = sharedPref.getStartTime().toString()
        val timenow = LocalTime.now()
        val splitan = timenow.toString().split(".").toTypedArray()
        val time = splitan[0]
        val realtimesplit = time.split(":").toTypedArray()
        val downtimesplit = startTime.split(":").toTypedArray()
        Log.e("starttimeCOunter", startTime)

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
        var jam: Int = selisihwaktu / 3600

        selisihwaktu %= 3600
        var menit: Int
        var second: Int
        if (selisihwaktu >= 60) {
            menit = selisihwaktu / 60
            second = selisihwaktu % 60
        } else {
            menit = 0
            second = selisihwaktu
        }
        val count = object : CountDownTimer(timenow.nano.toLong(),1000){
            override fun onTick(millisUntilFinished: Long) {

                second+=1
                if(second>59){
                    second=0
                    menit+=1
                }
                if(menit>59){
                    menit=0
                    jam+=1
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

                sec = if (second < 10) {
                    "0$second"
                } else {
                    "$second"
                }

                waktu = "$hour:$minute:$sec"
                timerr.text = waktu
            }
            override fun onFinish() {
            }
        }
        count.start()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, LiveMonitoringActivity::class.java))
    }
}