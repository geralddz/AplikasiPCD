package com.app.trackingqrcode.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.app.trackingqrcode.R
import com.app.trackingqrcode.api.ApiUtils
import com.app.trackingqrcode.api.SharedPref
import com.app.trackingqrcode.response.DetailPlanResponse
import com.app.trackingqrcode.socket.BaseSocket
import com.app.trackingqrcode.socket.ListenDataSocket
import kotlinx.android.synthetic.main.activity_detail_part.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPlanActivity : BaseSocket() {
    private lateinit var id_plan: String
    private lateinit var sharedPref: SharedPref
    private lateinit var status: String
    private lateinit var stationname: String
    private lateinit var partname: String


    companion object {
        const val PartName = "partname"
        const val SKU = "sku"
        const val Startprod = "start"
        const val LastFinish = "finish"
        const val SPH = "SPH"
        const val TARGET = "TARGET"
        const val KEY_PLAN = "id_plan"
        const val SHIFT = "shift"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_part)

        sharedPref = SharedPref(this)
        backSum.setOnClickListener {
            startActivity(Intent(this,DetailStationActivity::class.java))
        }

        connectToSocket()

        status = sharedPref.getStatus().toString()
        stationname = sharedPref.getStation().toString()
        partname = sharedPref.getPartname().toString()
        id_plan = intent.getStringExtra(KEY_PLAN).toString()
        Log.e("plan", "onResponse: $id_plan")

        shiftStation.text = intent.getStringExtra(SHIFT).toString()
        namaStation.text = stationname
        statusStation.text = status
        tvsku.text = intent.getStringExtra(SKU).toString()
        tvpn.text = intent.getStringExtra(PartName).toString()
        tvstart.text = intent.getStringExtra(Startprod).toString()
        tvfinish.text = intent.getStringExtra(LastFinish).toString()
        tvsph.text = intent.getStringExtra(SPH).toString()
        tvtarget.text = intent.getStringExtra(TARGET).toString()
        showDetailPlan()
    }

    private fun showDetailPlan(){
        val retro = ApiUtils().getUserService()
        retro.getDetailPlan(id_plan).enqueue(object : Callback<DetailPlanResponse>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<DetailPlanResponse>, response: Response<DetailPlanResponse>) {
                val detailplan = response.body()
                Vefficiency.text = detailplan?.efficiency.toString()+"%"
                Voee.text = detailplan?.oee.toString()+"%"
                availibility.text = detailplan?.avaibility.toString()+"%"
                performance.text = detailplan?.performance.toString()+"%"
            }

            override fun onFailure(call: Call<DetailPlanResponse>, t: Throwable) {
                Log.e("Error", t.message!!)
            }

        })
    }

    override fun onResume() {
        super.onResume()
        initLiveDataListener()
    }
    private fun initLiveDataListener() {
        receivedEvent.observe(this) {
            displayEventData(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disconnectFromSocket()
    }

    private fun displayEventData(event: Any) {
        if (event is ListenDataSocket) {
            labeltv.apply {
                val newText = event.message + "\n" + this.text.toString()
                text = newText
            }
        }
    }
}