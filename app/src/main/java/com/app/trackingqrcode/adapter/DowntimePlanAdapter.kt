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

class DowntimePlanAdapter(private val dataDowntime: List<DataDowntime?>?): RecyclerView.Adapter<DowntimePlanAdapter.MyViewHolder>() {
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
        if (time!=null){
            val totaltime = (time.div(60))
            val number3digits = (totaltime.times(1000.0)).roundToInt() / 1000.0
            val number2digits = (number3digits * 100.0).roundToInt() / 100.0
            val downtime = (number2digits * 10.0).roundToInt() / 10.0
            holder.txttime.text = "$downtime Menit"
        }else{
            holder.txttime.text = "0 Menit"
        }
        holder.txtname.text = nama
    }

    override fun getItemCount(): Int {
        return dataDowntime!!.size
    }
}