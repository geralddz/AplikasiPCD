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
import kotlinx.android.synthetic.main.fragment_shift3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentShift3 : Fragment() {
    private var shift3data: MutableList<DetailStationData> = ArrayList()

    companion object {
        var idStation: String? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        idStation = id_station
        Log.e("station", "onResponse: $idStation")
        return inflater.inflate(R.layout.fragment_shift3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showDataShift3()
    }

    private fun showDataShift3(){
        val retro = ApiUtils().getUserService()
        retro.getDetailStation(idStation!!).enqueue(object :
            Callback<DetailStationResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<DetailStationResponse>, response: Response<DetailStationResponse>) {
                val detailstation = response.body()
                val detailstationdata = detailstation?.data
                    for (d in detailstationdata!!.indices){
                        val startpro = detailstationdata[d].startTime
                        val finishpro = detailstationdata[d].finishTime
                        val dtstartpro = startpro?.split(" ".toRegex())?.toTypedArray()
                        val dtfinishpro = finishpro?.split(" ".toRegex())?.toTypedArray()
                        val timeStartpro = dtstartpro?.get(1).toString()
                        val timeFinishpro = dtfinishpro?.get(1).toString()
                        val startShift3 = "18:00:00"
                        val startShift3final = "23:59:59"
                        val finishShift3 = "00:00:00"
                        val finishShift3Final = "06:59:59"
                        if ((timeStartpro in startShift3..startShift3final)||(timeFinishpro in finishShift3..finishShift3Final)){
                            val sku = detailstationdata[d].sku
                            val partname = detailstationdata[d].partName
                            val operator = detailstationdata[d].statusFlag
                            val sph = detailstationdata[d].cycleTime
                            val totaltime = detailstationdata[d].totalTime
                            val planid = detailstationdata[d].planId
                            try {
                                shift3data.addAll(listOf(DetailStationData(startpro,operator,sph,totaltime,sku,finishpro,planid,partname,"Shift 3")))
                                val detailstationadapter = DetailStationAdapter(context!!, shift3data)
                                rvshift3.apply {
                                    layoutManager = LinearLayoutManager(context)
                                    setHasFixedSize(true)
                                    adapter = detailstationadapter
                                    detailstationadapter.notifyDataSetChanged()
                                }
                            } catch (e: Exception) {
                                Log.e("shift3", "error",e)
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