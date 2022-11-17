package com.app.trackingqrcode.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.trackingqrcode.R
import com.app.trackingqrcode.response.DataRejectionPlan
import kotlinx.android.synthetic.main.item_plan.view.*

class RejectionPlanAdapter (val dataRejectionPlan: List<DataRejectionPlan>?): RecyclerView.Adapter<RejectionPlanAdapter.MyViewHolder>() {
    class MyViewHolder (view: View):RecyclerView.ViewHolder(view){
        val txtname = view.txt2
        val txtValue = view.txt4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plan,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txtname.text = dataRejectionPlan?.get(position)?.name
        holder.txtValue.text = dataRejectionPlan?.get(position)?.value.toString()    }

    override fun getItemCount(): Int {
        return dataRejectionPlan!!.size
    }

}