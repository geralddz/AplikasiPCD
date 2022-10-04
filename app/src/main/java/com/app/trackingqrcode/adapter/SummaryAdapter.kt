package com.app.trackingqrcode.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.app.trackingqrcode.R
import com.app.trackingqrcode.activity.DetailSummaryActivity
import com.app.trackingqrcode.model.SummaryList
import kotlinx.android.synthetic.main.item_summary.view.*


class SummaryAdapter(var context: Context, val datasummary: List<SummaryList>): RecyclerView.Adapter<SummaryAdapter.MyViewHolder>() {

    companion object {
        private const val KEY_STATION = "id_station"
        private const val KEY_PRODUK = "id_produk"
        private const val STATION = "station"
        private const val CUSTOMER = "customer"
    }

    class MyViewHolder (view: View):RecyclerView.ViewHolder(view){
        val station_num = view.Htstation
        val part_name = view.Htpart
        val cv = view.cdsummary
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutinflater = LayoutInflater.from(parent.context)
        val view = layoutinflater.inflate(R.layout.item_summary,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.station_num.text = datasummary[position].stationNum
        holder.part_name.text = datasummary[position].partName

        holder.cv.setOnClickListener {
            val intent = Intent(it.context, DetailSummaryActivity::class.java)
            intent.putExtra(KEY_STATION, datasummary[position].id_station.toString())
            intent.putExtra(KEY_PRODUK, datasummary[position].id_produk.toString())
            intent.putExtra(CUSTOMER, datasummary[position].customer)
            intent.putExtra(STATION, holder.station_num.text)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return datasummary.size
    }
}