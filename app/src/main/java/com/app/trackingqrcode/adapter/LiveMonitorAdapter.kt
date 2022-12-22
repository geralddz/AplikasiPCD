package com.app.trackingqrcode.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.trackingqrcode.activity.DetailStationActivity
import com.app.trackingqrcode.R
import com.app.trackingqrcode.activity.LiveMonitoringActivity
import com.app.trackingqrcode.utils.SharedPref
import com.app.trackingqrcode.model.LiveMonitorData
import com.app.trackingqrcode.response.DataLive
import com.app.trackingqrcode.response.LiveStationResponse
import kotlinx.android.synthetic.main.item_station.view.*
import retrofit2.Callback

class LiveMonitorAdapter(var context: Context, private val datamonitoring: List<DataLive?>?): RecyclerView.Adapter<LiveMonitorAdapter.MyViewHolder>(){

    private lateinit var sharedPref: SharedPref

    class MyViewHolder (view: View):RecyclerView.ViewHolder(view){
        val station_num = view.station
        val part_name = view.part
        val cs = view.cardstation
        val card = view.card

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutinflater = LayoutInflater.from(parent.context)
        val view = layoutinflater.inflate(R.layout.item_station,parent,false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        sharedPref = SharedPref(context)
        val id_plan = datamonitoring?.get(position)?.planningId.toString()
        val id_station = datamonitoring?.get(position)?.stationId.toString()
        val station_nama = datamonitoring?.get(position)?.name.toString()
        val status = datamonitoring?.get(position)?.status
        val partname = datamonitoring?.get(position)?.partName.toString()
        val downtime = datamonitoring?.get(position)?.isDowntime

        holder.station_num.text = station_nama
        holder.part_name.text = partname
        holder.part_name.setTypeface(null,Typeface.BOLD)

        if (status=="Start"){
            if (downtime==1){
                holder.cs.setBackgroundResource(R.drawable.bgyellow)
                holder.station_num.setTextColor(Color.BLACK)
                holder.part_name.setTextColor(Color.BLACK)
                holder.card.setOnClickListener {
                    val intent = Intent(it.context, DetailStationActivity::class.java)
                    sharedPref.setStation(station_nama)
                    sharedPref.setIdPlan(id_plan)
                    sharedPref.setIdStation(id_station)
                    sharedPref.setStatus(status)
                    sharedPref.setPartname(partname)
                    sharedPref.setIsDowntime(downtime.toString())
                    it.context.startActivity(intent)
                }
            }else{
                holder.cs.setBackgroundResource(R.drawable.bggreen)
                holder.station_num.setTextColor(Color.BLACK)
                holder.part_name.setTextColor(Color.BLACK)
                holder.card.setOnClickListener {
                    val intent = Intent(it.context, DetailStationActivity::class.java)
                    sharedPref.setStation(station_nama)
                    sharedPref.setIdPlan(id_plan)
                    sharedPref.setIdStation(id_station)
                    sharedPref.setStatus(status)
                    sharedPref.setIsDowntime(downtime.toString())
                    sharedPref.setPartname(partname)
                    it.context.startActivity(intent)
                }
            }
        }else{
            holder.cs.setBackgroundResource(R.drawable.bgred)
            holder.station_num.setTextColor(Color.WHITE)
            holder.part_name.text = "STOPPED"
            holder.part_name.setTextColor(Color.WHITE)

            holder.card.setOnClickListener {
                val intent = Intent(it.context, DetailStationActivity::class.java)
                sharedPref.setStation(station_nama)
                sharedPref.setIdPlan(id_plan)
                sharedPref.setIdStation(id_station)
                sharedPref.setIsDowntime(downtime.toString())
                if (status != null) {
                    sharedPref.setStatus(status)
                }
                sharedPref.setPartname(partname)
                it.context.startActivity(intent)
            }
        }
    }
    override fun getItemCount(): Int {
        return datamonitoring!!.size
    }
}