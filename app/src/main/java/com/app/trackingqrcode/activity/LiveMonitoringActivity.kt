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
import com.app.trackingqrcode.response.DowntimeResponse
import com.app.trackingqrcode.response.OnPlanningResponse
import com.app.trackingqrcode.response.StationResponse
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
    private var livemonitordata: MutableList<LiveMonitorData> = ArrayList()
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
    private fun setAdapter(list: List<LiveMonitorData>){
        val adapterLiveMonitor = LiveMonitorAdapter(this, list)
        rvLive.adapter = adapterLiveMonitor
        adapterLiveMonitor.notifyDataSetChanged()
    }

    private fun queryStation(selectedStation: String): List<LiveMonitorData>{
        val filteredList: MutableList<LiveMonitorData> = ArrayList()
        for (item in livemonitordata) {
            if (item.nama_station!!.lowercase(Locale.ROOT).contains(selectedStation.lowercase())) {
                filteredList.add(item)
            }
        }
        return filteredList
    }
    private fun queryStop(): List<LiveMonitorData>{
        val filteredList: MutableList<LiveMonitorData> = ArrayList()
        for (item in livemonitordata) {
            if (item.status!!.contains(selectedStop)) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun queryStart(): List<LiveMonitorData>{
        val filteredList: MutableList<LiveMonitorData> = ArrayList()
        for (item in livemonitordata) {
            if (item.status!!.contains(selectedStart)) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun queryProblem(): List<LiveMonitorData>{
        val filteredList: MutableList<LiveMonitorData> = ArrayList()
        for (item in livemonitordata) {
            if (item.status!!.contains(selectedProblem)) {
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
    private fun countStation(list: List<LiveMonitorData>) {
        var cgreen = 0
        var cyellow = 0
        var cred = 0
        for (item in list) {
            when (item.status) {
                "start" -> {
                    cgreen += 1
                }
                "problem" -> {
                    cyellow += 1
                }
                else -> {
                    cred +=1
                }
            }
        }
        green.text = "$cgreen Station"
        yellow.text = "$cyellow Station"
        red.text = "$cred Station"
    }

    @SuppressLint("SetTextI18n")
    fun showStation(){
        val retro = ApiUtils().getUserService()
        green.text = "0 Station"
        yellow.text = "0 Station"
        red.text = "0 Station"
        val countRed = intArrayOf(0)
        val countGreen = intArrayOf(0)
        val countYellow = intArrayOf(0)
        retro.getStation()!!.enqueue(object : Callback<StationResponse>{
            override fun onResponse(call: Call<StationResponse>, response: Response<StationResponse>) {
                val station = response.body()?.data
                for (s in station!!.indices){
                    val stationid = station[s].id
                    val stationname = station[s].name
                    val sumCG = arrayOf(countGreen[0])
                    val sumCY = arrayOf(countYellow[0])
                    val sumCR = arrayOf(countRed[0])
                    retro.getOnPlanning(stationid).enqueue(object : Callback<OnPlanningResponse> {
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onResponse(call: Call<OnPlanningResponse>, response: Response<OnPlanningResponse>) {
                            val planning = response.body()
                            val planningdata = planning?.data
                            val planid = planningdata?.get(0)?.planningId
                            val partname = planningdata?.get(0)?.partName
                            if (planning?.success == true) {
                                retro.getDowntime(stationid.toString(), planid.toString()).enqueue(object : Callback<DowntimeResponse> {
                                    override fun onResponse(call: Call<DowntimeResponse>, response: Response<DowntimeResponse>) {
                                        val downtime = response.body()
                                        val downtimedata = downtime?.data
                                        var onplan = true
                                        for(d in downtimedata!!.indices){
                                            val statusdown = downtimedata[d]?.status
                                            val planiddown = downtimedata[d]?.planningId
                                            val downtimeCt = downtimedata[d]?.downtimeCategory
                                            downtimecty = downtimeCt.toString()
                                            val startt = downtimedata[d]?.startTime.toString()
                                            val splitan = startt.split(" ").toTypedArray()
                                            startTime = splitan[1]

                                            if (planid==planiddown&&statusdown==0){
                                                onplan = false
                                            }
                                        }
                                        if (onplan){
                                            stationstatus = "start"
                                            livemonitordata.addAll(listOf(LiveMonitorData(stationid, stationname, partname, planid, stationstatus,"","")))
                                            Log.e("idstation","$stationid")
                                            sumCG[0] += 1
                                            green.text = "${sumCG[0]} Station"
                                        }else{
                                            stationstatus = "problem"
                                            livemonitordata.addAll(listOf(LiveMonitorData(stationid, stationname, partname, planid, stationstatus,startTime,downtimecty)))
                                            Log.e("idstation","$stationid")
                                            sumCY[0] += 1
                                            yellow.text = "${sumCY[0]} Station"
                                        }
                                        val liveadapter = LiveMonitorAdapter(this@LiveMonitoringActivity, livemonitordata)
                                        rvLive.apply {
                                            layoutManager =
                                                LinearLayoutManager(this@LiveMonitoringActivity)
                                            setHasFixedSize(true)
                                            adapter = liveadapter
                                            liveadapter.notifyDataSetChanged()
                                        }
                                        countStation(livemonitordata)
                                    }
                                    override fun onFailure(call: Call<DowntimeResponse>, t: Throwable) {
                                        Log.e("Error", t.message!!)
                                    }
                                })
                            }else {
                                stationstatus = "stop"
                                livemonitordata.addAll(listOf(LiveMonitorData(stationid, stationname, partname, planid, stationstatus,"","")))
                                Log.e("idstation","$stationid")
                                sumCR[0] += 1
                                red.text = "${sumCR[0]} Station"
                            }
                            val liveadapter = LiveMonitorAdapter(
                                this@LiveMonitoringActivity,
                                livemonitordata
                            )
                            rvLive.apply {
                                layoutManager =
                                    LinearLayoutManager(this@LiveMonitoringActivity)
                                setHasFixedSize(true)
                                adapter = liveadapter
                                liveadapter.notifyDataSetChanged()
                            }
                            countStation(livemonitordata)
                            stopShimmer()
                            if (!Lstation.contains(stationname)) {
                                Lstation.add(stationname!!)
                            }
                        }
                        override fun onFailure(call: Call<OnPlanningResponse>, t: Throwable) {
                            Log.e("Error", t.message!!)
                        }
                    })
                }

            }
            override fun onFailure(call: Call<StationResponse>, t: Throwable) {
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

