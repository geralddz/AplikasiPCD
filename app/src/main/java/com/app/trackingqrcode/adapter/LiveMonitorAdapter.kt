package com.app.trackingqrcode.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.trackingqrcode.R
import com.app.trackingqrcode.model.LiveMonitorData
import kotlinx.android.synthetic.main.item_station.view.*

class LiveMonitorAdapter(var context: Context, val datamonitoring: List<LiveMonitorData>): RecyclerView.Adapter<LiveMonitorAdapter.MyViewHolder>(){

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

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.station_num.text = datamonitoring[position].nama_station
        holder.part_name.text = datamonitoring[position].nama_part
        if (datamonitoring[position].status == "stop"){
            holder.cs.background = this.context.getDrawable(R.drawable.bgred)
        }else{

        }

    }

    override fun getItemCount(): Int {
        return datamonitoring.size
    }
}