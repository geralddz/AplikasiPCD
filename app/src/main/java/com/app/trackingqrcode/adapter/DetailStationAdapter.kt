package com.app.trackingqrcode.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.trackingqrcode.activity.DetailPlanActivity
import com.app.trackingqrcode.R
import com.app.trackingqrcode.model.DetailStationData
import kotlinx.android.synthetic.main.activity_detail_part.view.*
import kotlinx.android.synthetic.main.item_detail_station.view.*
import kotlinx.android.synthetic.main.item_detail_station.view.tvfinish
import kotlinx.android.synthetic.main.item_detail_station.view.tvop
import kotlinx.android.synthetic.main.item_detail_station.view.tvpn
import kotlinx.android.synthetic.main.item_detail_station.view.tvsku
import kotlinx.android.synthetic.main.item_detail_station.view.tvsph
import kotlinx.android.synthetic.main.item_detail_station.view.tvstart
import kotlinx.android.synthetic.main.item_detail_station.view.tvtarget

class DetailStationAdapter(var context: Context , val dataDetailStation : List<DetailStationData>): RecyclerView.Adapter<DetailStationAdapter.MyViewHolder>(){
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


    class MyViewHolder (view: View):RecyclerView.ViewHolder(view){
        val sku = view.tvsku
        val pn = view.tvpn
        val op = view.tvop
        val startprod = view.tvstart
        val lastfinish = view.tvfinish
        val sph = view.tvsph
        val targett = view.tvtarget
        val card = view.carddetailstation
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_detail_station,parent,false)
        return MyViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val shift = dataDetailStation[position].shift
        holder.pn.text = dataDetailStation[position].partName
        holder.startprod.text = dataDetailStation[position].startTime
        holder.lastfinish.text= dataDetailStation[position].finishTime
        holder.sku.text = dataDetailStation[position].sku
        holder.op.text= dataDetailStation[position].statusFlag
        val totaltime = dataDetailStation[position].totalTime
        val ct = dataDetailStation[position].cycleTime
        val sph = 3600.div(ct!!)
        val target = totaltime?.div(sph)
        holder.sph.text = sph.toString()
        holder.targett.text = target.toString()
        holder.card.setOnClickListener {

            val intent = Intent(it.context, DetailPlanActivity::class.java)
            intent.putExtra(PartName,dataDetailStation[position].partName)
            intent.putExtra(SKU,dataDetailStation[position].sku)
            intent.putExtra(Startprod,dataDetailStation[position].startTime)
            intent.putExtra(LastFinish,dataDetailStation[position].finishTime)
            intent.putExtra(KEY_PLAN,dataDetailStation[position].planId.toString())
            intent.putExtra(SPH,holder.sph.text)
            intent.putExtra(TARGET,holder.targett.text)
            intent.putExtra(SHIFT,shift.toString())
            it.context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return dataDetailStation.size
    }

}

