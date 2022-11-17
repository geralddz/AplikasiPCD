package com.app.trackingqrcode.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.trackingqrcode.R
import com.app.trackingqrcode.activity.DetailStationActivity.Companion.id_station
import com.app.trackingqrcode.adapter.DetailStationAdapter
import com.app.trackingqrcode.api.ApiUtils
import com.app.trackingqrcode.model.DetailStationData
import com.app.trackingqrcode.response.DetailStationResponse
import kotlinx.android.synthetic.main.fragment_shift1.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import kotlin.math.log

class FragmentShift1 : Fragment() {

    private var shift1data: MutableList<DetailStationData> = ArrayList()

    companion object {
        var idStation: String? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        idStation = id_station
        Log.e("station", "onResponse: $idStation")
        return inflater.inflate(R.layout.fragment_shift1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showDataShift1()
    }

    private fun showDataShift1() {
        val retro = ApiUtils().getUserService()
        retro.getDetailStation(idStation!!).enqueue(object : Callback<DetailStationResponse> {
            @SuppressLint("NotifyDataSetChanged", "SuspiciousIndentation")
            override fun onResponse(call: Call<DetailStationResponse>, response: Response<DetailStationResponse>) {
                val detailstation = response.body()
                val detailstationdata = detailstation?.data
                for (d in detailstationdata!!.indices) {
                    val startpro = detailstationdata[d].startTime
                    val finishpro = detailstationdata[d].finishTime
                    val dtstartpro = startpro?.split(" ".toRegex())?.toTypedArray()
                    val dtfinishpro = finishpro?.split(" ".toRegex())?.toTypedArray()
                    val timeStartpro = dtstartpro?.get(1).toString()
                    val timeFinishpro = dtfinishpro?.get(1).toString()
                    val startShift1 = "06:00:00"
                    val finishShift1 = "17:59:59"
                    if (timeStartpro<finishShift1){
                        if((timeStartpro in startShift1..finishShift1)||(timeFinishpro in startShift1..finishShift1)){
                            val sku = detailstationdata[d].sku
                            val partname = detailstationdata[d].partName
                            val operator = detailstationdata[d].statusFlag
                            val sph = detailstationdata[d].cycleTime
                            val totaltime = detailstationdata[d].totalTime
                            val planid = detailstationdata[d].planId

                            try {
                                shift1data.addAll(listOf(DetailStationData(startpro,operator,sph,totaltime,sku,finishpro,planid,partname,"Shift 1")))
                                val detailstationadapter = DetailStationAdapter(context!!, shift1data)
                                rvshift1.apply {
                                    layoutManager = LinearLayoutManager(context)
                                    setHasFixedSize(true)
                                    adapter = detailstationadapter
                                    detailstationadapter.notifyDataSetChanged()
                                }
                            } catch (e: Exception) {
                                Log.e("shift1", "error", e)
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<DetailStationResponse>, t: Throwable) {
                Log.e("Error", t.message!!)
            }
        })
    }
}