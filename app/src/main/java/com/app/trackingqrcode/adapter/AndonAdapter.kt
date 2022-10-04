package com.app.trackingqrcode.adapter

import android.app.Dialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.trackingqrcode.R
import com.app.trackingqrcode.model.AndonData
import kotlinx.android.synthetic.main.dialog_assign_andon.*
import kotlinx.android.synthetic.main.item_andon.view.*

private var tambaheskalasi : Int = 1

class AndonAdapter(private var Andon: List<AndonData>) : RecyclerView.Adapter<AndonAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtstation = itemView.head
        val txtreason = itemView.reason
        val txtTandai = itemView.dibaca
        val txtTime = itemView.time
        val eskalasi = itemView.progressBar
        val btnAssign = itemView.assign


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_andon, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txtstation.text = Andon?.get(position)?.head
        holder.txtreason.text = Andon?.get(position)?.reason
        holder.txtTime.text = Andon?.get(position)?.time

        holder.btnAssign.setOnClickListener {
            val dialog =  Dialog(it.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.dialog_assign_andon)
            dialog.btnOK.setOnClickListener {
                holder.eskalasi.curValue = tambaheskalasi++
                dialog.dismiss()
                Toast.makeText(it.context, "Berhasil", Toast.LENGTH_SHORT
                ).show()            }
            dialog.btnBatal.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()

        }
        holder.txtTandai.setOnClickListener {
            holder.txtTandai.text = "Telah Dibaca"
            holder.txtTandai.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount(): Int {
        return Andon.size
    }

}


