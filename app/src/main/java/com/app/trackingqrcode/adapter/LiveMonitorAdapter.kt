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
import com.app.trackingqrcode.utils.SharedPref
import com.app.trackingqrcode.model.LiveMonitorData
import kotlinx.android.synthetic.main.item_station.view.*

class LiveMonitorAdapter(var context: Context, private val datamonitoring: List<LiveMonitorData>): RecyclerView.Adapter<LiveMonitorAdapter.MyViewHolder>(){

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
        val id_plan = datamonitoring[position].id_plan.toString()
        val id_station = datamonitoring[position].id_station.toString()
        val station_nama = datamonitoring[position].nama_station.toString()
        val status = datamonitoring[position].status.toString()
        val partname = datamonitoring[position].partname.toString()
        val downtimecty = datamonitoring[position].downtimecty.toString()
        val starttime = datamonitoring[position].starttime

        holder.station_num.text = station_nama
        holder.part_name.text = partname
        holder.part_name.setTypeface(null,Typeface.BOLD)

        when (status) {
            "stop" -> {
                holder.cs.setBackgroundResource(R.drawable.bgred)
                holder.station_num.setTextColor(Color.WHITE)
                holder.part_name.text = "STOPPED"
                holder.part_name.setTextColor(Color.WHITE)

                holder.card.setOnClickListener {
                    val intent = Intent(it.context, DetailStationActivity::class.java)
                    sharedPref.setStation(station_nama)
                    sharedPref.setIdPlan(id_plan)
                    sharedPref.setIdStation(id_station)
                    sharedPref.setStatus(status)
                    sharedPref.setPartname(partname)
                    it.context.startActivity(intent)
                }
            }
            "problem" -> {
                holder.cs.setBackgroundResource(R.drawable.bgyellow)

                holder.card.setOnClickListener {
                    val intent = Intent(it.context, DetailStationActivity::class.java)
                    sharedPref.setStation(station_nama)
                    sharedPref.setIdPlan(id_plan)
                    sharedPref.setIdStation(id_station)
                    sharedPref.setStatus(status)
                    sharedPref.setPartname(partname)
                    sharedPref.setStartTime(starttime!!)
                    sharedPref.setDowntimeCategory(downtimecty)
                    it.context.startActivity(intent)
                }
            }
            else -> {
                holder.card.setOnClickListener {
                    val intent = Intent(it.context, DetailStationActivity::class.java)
                    sharedPref.setStation(station_nama)
                    sharedPref.setIdPlan(id_plan)
                    sharedPref.setIdStation(id_station)
                    sharedPref.setStatus(status)
                    sharedPref.setPartname(partname)
                    it.context.startActivity(intent)
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return datamonitoring.size
    }
}