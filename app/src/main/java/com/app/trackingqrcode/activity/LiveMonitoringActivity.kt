package com.app.trackingqrcode.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.trackingqrcode.R
import com.app.trackingqrcode.adapter.LiveMonitorAdapter
import com.app.trackingqrcode.api.ApiUtils
import com.app.trackingqrcode.model.LiveMonitorData
import com.app.trackingqrcode.response.DataStation
import com.app.trackingqrcode.response.OnPlanningResponse
import com.app.trackingqrcode.response.StationResponse
import com.app.trackingqrcode.socket.BaseSocket
import com.app.trackingqrcode.socket.ListenDataSocket
import kotlinx.android.synthetic.main.activity_live_monitoring.*
import kotlinx.android.synthetic.main.activity_live_monitoring.back
import kotlinx.android.synthetic.main.activity_live_monitoring.labeltv
import kotlinx.android.synthetic.main.activity_summary.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class LiveMonitoringActivity : BaseSocket(){

    private var stationid: MutableList<DataStation> = ArrayList()
    private var livemonitordata: MutableList<LiveMonitorData> = ArrayList()
    private var adapter: LiveMonitorAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_monitoring)

        back.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
//
//        connectToSocket()
//        initLiveDataListener()
        setShimmer()
        showStation()

    }
    private fun initLiveDataListener() {
        receivedEvent.observe(this, Observer {
            displayEventData(it)
        })
    }

    private fun displayEventData(event: Any) {
        if (event is ListenDataSocket) {
            labeltv.apply {
                val newText = event.message + "\n" + this.text.toString()
                text = newText
            }
        }
    }

    fun showStation(){
        val retro = ApiUtils().getUserService()
        retro.getStation()?.enqueue(object : Callback<StationResponse>{
            override fun onResponse(call: Call<StationResponse>, response: Response<StationResponse>) {
                val station = response.body()?.data
                stationid.addAll(station!!)
                getplanning(stationid)

            }

            override fun onFailure(call: Call<StationResponse>, t: Throwable) {
                Log.e("Error", t.message!!)
            }

        })
    }

    fun getplanning(station: List<DataStation>) {
        val retro = ApiUtils().getUserService()
        for (s in station.indices) {
            val stationid = station[s].id
            Log.e("stationid :", stationid.toString())
            val stationname = station[s].name
            retro.getOnPlanning(station[s].id).enqueue(object : Callback<OnPlanningResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<OnPlanningResponse>, response: Response<OnPlanningResponse>) {
                    val planning = response.body()
                    val planningdata = planning?.data
                        for (i in planningdata!!.indices) {
                            val partname = planningdata[i]?.partName
                            val planid = planningdata[i]?.planningId
                            livemonitordata.addAll(listOf(LiveMonitorData(stationid!!, stationname!!, partname!!, planid!!, "start")))
                        }


                    val liveadapter = LiveMonitorAdapter(
                        this@LiveMonitoringActivity,
                        livemonitordata as List<LiveMonitorData>
                    )
                    rvLive.apply {
                        layoutManager =
                            LinearLayoutManager(this@LiveMonitoringActivity)
                        setHasFixedSize(true)
                        adapter = liveadapter
                        liveadapter.notifyDataSetChanged()
                    }

                    stopShimmer()
                }
                override fun onFailure(call: Call<OnPlanningResponse>, t: Throwable) {
                    Log.e("Error", t.message!!)
                }

            })
        }
    }

    fun setShimmer() {
        shimmerlayout.showShimmer(true)
        shimmerlayout.visibility = View.VISIBLE
        rvLive.visibility = View.GONE

    }
    fun stopShimmer() {
        shimmerlayout.stopShimmer()
        shimmerlayout.visibility = View.GONE
        rvLive.visibility = View.VISIBLE

    }
}

