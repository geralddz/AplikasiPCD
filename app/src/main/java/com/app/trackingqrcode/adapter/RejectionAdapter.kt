package com.app.trackingqrcode.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.app.trackingqrcode.R
import com.app.trackingqrcode.model.RejectionItem
import kotlinx.android.synthetic.main.item_plan.view.*

class RejectionAdapter (val datarejection : List<RejectionItem?>?): RecyclerView.Adapter<RejectionAdapter.MyViewHolder>(){
    class MyViewHolder (view: View):RecyclerView.ViewHolder(view){
        val txtname = view.txt2
        val txtReject = view.txt4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_plan,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txtname.text = datarejection?.get(position)?.name
        holder.txtReject.text = datarejection?.get(position)?.reject.toString()

    }

    override fun getItemCount(): Int {
        return datarejection!!.size
    }


}