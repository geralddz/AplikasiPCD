package com.app.trackingqrcode.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.trackingqrcode.R
import com.app.trackingqrcode.adapter.LiveMonitorAdapter
import com.app.trackingqrcode.utils.ApiUtils
import com.app.trackingqrcode.model.LiveMonitorData
import com.app.trackingqrcode.response.*
import kotlinx.android.synthetic.main.activity_detail_station.*
import kotlinx.android.synthetic.main.activity_live_monitoring.*
import kotlinx.android.synthetic.main.activity_live_monitoring.back
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LiveMonitoringActivity : AppCompatActivity(){

    var stationstatus: String? = null
    private lateinit var downtimecty: String
    private var adapter: LiveMonitorAdapter? = null
    private val Lstation = ArrayList<String>()
    private lateinit var selectedStation: String
    private var selectedStop = "stop"
    private var selectedStart = "start"
    private var selectedProblem = "problem"
    private var livemonitordata: MutableList<DataLive> = ArrayList()
    private lateinit var startTime: String

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_monitoring)
        back.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        hintStart.setOnClickListener {
            setAdapter(queryStart())
        }

        hintProblem.setOnClickListener {
            setAdapter(queryProblem())
        }

        hintStop.setOnClickListener {
            setAdapter(queryStop())
        }

        setShimmer()
        showStation()

        swiperlive.setOnRefreshListener {
            setShimmer()
            livemonitordata = ArrayList()
            adapter?.notifyDataSetChanged()
            showStation()
            swiperlive.isRefreshing = false
            val liveadapter = LiveMonitorAdapter(
                this@LiveMonitoringActivity, livemonitordata
            )
            rvLive.apply {
                layoutManager = LinearLayoutManager(this@LiveMonitoringActivity)
                setHasFixedSize(true)
                adapter = liveadapter
                liveadapter.notifyDataSetChanged()
            }
        }

        val dataStation = ArrayAdapter(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, Lstation)
        filterstation.setAdapter(dataStation)
        filterstation.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                selectedStation = s.toString()
                filter(selectedStation)
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(list: List<DataLive>){
        val adapterLiveMonitor = LiveMonitorAdapter(this, list)
        rvLive.adapter = adapterLiveMonitor
        adapterLiveMonitor.notifyDataSetChanged()
    }

    private fun queryStation(selectedStation: String): List<DataLive>{
        val filteredList: MutableList<DataLive> = ArrayList()
        for (item in livemonitordata) {
            if (item.name!!.lowercase(Locale.ROOT).contains(selectedStation.lowercase())) {
                filteredList.add(item)
            }
        }
        return filteredList
    }
    private fun queryStop(): List<DataLive>{
        val filteredList: MutableList<DataLive> = ArrayList()
        for (item in livemonitordata) {
            if ((item.status==null||item.status=="Finish") && item.isDowntime==0) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun queryStart(): List<DataLive>{
        val filteredList: MutableList<DataLive> = ArrayList()
        for (item in livemonitordata) {
            if (item.status=="Start" && item.isDowntime==0) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun queryProblem(): List<DataLive>{
        val filteredList: MutableList<DataLive> = ArrayList()
        for (item in livemonitordata) {
            if (item.status=="Start" && item.isDowntime==1) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun filter(selectedStation: String){
        val station  = queryStation(selectedStation)
        setAdapter(station)
    }

    @SuppressLint("SetTextI18n")
    private fun countStation(list: List<DataLive>) {
        var cgreen = 0
        var cyellow = 0
        var cred = 0
        for (item in list) {
            if (item.status=="Start"){
                if (item.isDowntime==1){
                    cyellow += 1
                }else{
                    cgreen += 1
                }
            }else {
                cred += 1
            }
        }
        green.text = "$cgreen Station"
        yellow.text = "$cyellow Station"
        red.text = "$cred Station"
    }

    private fun showStation(){
        green.text = "0 Station"
        yellow.text = "0 Station"
        red.text = "0 Station"
        val countRed = intArrayOf(0)
        val countGreen = intArrayOf(0)
        val countYellow = intArrayOf(0)
        val retro = ApiUtils().getUserService()
        retro.getLiveStation()?.enqueue(object : Callback<LiveStationResponse>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<LiveStationResponse>, response: Response<LiveStationResponse>) {
                val livemonitor = response.body()?.data
                livemonitordata = livemonitor as MutableList<DataLive>
                for (s in livemonitor.indices){
                    val sumCG = arrayOf(countGreen[0])
                    val sumCY = arrayOf(countYellow[0])
                    val sumCR = arrayOf(countRed[0])
                    val stationname = livemonitor[s].name
                    val status = livemonitor[s].status
                    val downtime = livemonitor[s].isDowntime
                    if (status=="Start"){
                       if(downtime==1){
                           sumCY[0] += 1
                           yellow.text = "${sumCY[0]} Station"
                       }else{
                           sumCG[0] += 1
                           green.text = "${sumCG[0]} Station"
                       }
                    }else{
                        sumCR[0] += 1
                        red.text = "${sumCR[0]} Station"
                    }
                    if (!Lstation.contains(stationname)) {
                        Lstation.add(stationname!!)
                    }
                }
                val livestationadapter = LiveMonitorAdapter(applicationContext, livemonitordata)
                rvLive.apply {
                    layoutManager = LinearLayoutManager(this@LiveMonitoringActivity)
                    setHasFixedSize(true)
                    adapter = livestationadapter
                    livestationadapter.notifyDataSetChanged()
                }
                countStation(livemonitordata as List<DataLive>)
                stopShimmer()

            }
            override fun onFailure(call: Call<LiveStationResponse>, t: Throwable) {
                Log.e("Error", t.message!!)
            }
        })
    }
    private fun setShimmer() {
        shimmerlayout.startShimmer()
        shimmerlayout1.startShimmer()
        shimmerlayout2.startShimmer()
        shimmerlayout3.startShimmer()
        shimmerlayout.visibility = View.VISIBLE
        shimmerlayout1.visibility = View.VISIBLE
        shimmerlayout2.visibility = View.VISIBLE
        shimmerlayout3.visibility = View.VISIBLE
        green.visibility = View.GONE
        yellow.visibility = View.GONE
        red.visibility = View.GONE
        rvLive.visibility = View.GONE
    }

    private fun stopShimmer() {
        shimmerlayout.stopShimmer()
        shimmerlayout1.stopShimmer()
        shimmerlayout2.stopShimmer()
        shimmerlayout3.stopShimmer()
        shimmerlayout.visibility = View.GONE
        shimmerlayout1.visibility = View.GONE
        shimmerlayout2.visibility = View.GONE
        shimmerlayout3.visibility = View.GONE
        green.visibility = View.VISIBLE
        yellow.visibility = View.VISIBLE
        red.visibility = View.VISIBLE
        rvLive.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent (applicationContext,HomeActivity::class.java)
        startActivity(intent)
    }
}