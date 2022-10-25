package com.app.trackingqrcode.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.app.trackingqrcode.R
import com.app.trackingqrcode.adapter.PagerAdapter
import com.app.trackingqrcode.api.SharedPref
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_detail_station.*

class DetailStationActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPref
    private lateinit var id_plan: String
    private lateinit var status: String
    private lateinit var stationname: String
    private lateinit var partname: String
    private var Tabtitle = arrayOf("shift 1", "shift 3")

    companion object {
        var id_station: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_station)
        sharedPref = SharedPref(this)
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
            }
            "problem" -> {
                problem.visibility = View.VISIBLE
                onprogress.visibility = View.GONE
                stopped.visibility = View.GONE
                dtcty.text = partname
            }
            else -> {
                problem.visibility = View.GONE
                onprogress.visibility = View.VISIBLE
                stopped.visibility = View.GONE
                station_on.text = stationname
                part_on.text = partname
            }
        }
        viewPager.adapter = PagerAdapter(supportFragmentManager, lifecycle)
        TabLayoutMediator(tab_layout, viewPager) { tab, position ->
            tab.text = Tabtitle[position]

        }.attach()


    }

}
