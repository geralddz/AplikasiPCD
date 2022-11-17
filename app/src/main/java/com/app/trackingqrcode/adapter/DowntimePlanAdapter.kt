package com.app.trackingqrcode.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.trackingqrcode.R
import com.app.trackingqrcode.response.DataDowntime
import kotlinx.android.synthetic.main.item_plan.view.*
import kotlin.math.roundToInt

class DowntimePlanAdapter(val dataDowntime: List<DataDowntime?>?): RecyclerView.Adapter<DowntimePlanAdapter.MyViewHolder>() {
    class MyViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val txtname = view.txt2
        val txttime = view.txt4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plan,parent,false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val nama = dataDowntime?.get(position)?.downtimeCategory.toString()
        val time = dataDowntime?.get(position)?.totalTime?.toFloat()
        val totaltime = (time?.div(60)!!)
        val number3digits = Math.round(totaltime * 1000.0) / 1000.0
        val number2digits = Math.round(number3digits * 100.0) / 100.0
        val downtime = Math.round(number2digits * 10.0) / 10.0
        holder.txtname.text = nama
        holder.txttime.text = "$downtime Menit"
    }

    override fun getItemCount(): Int {
        return dataDowntime!!.size
    }
}