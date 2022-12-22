package com.app.trackingqrcode.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.app.trackingqrcode.R
import com.app.trackingqrcode.response.DataAndon
import kotlinx.android.synthetic.main.dialog_assign_andon.*
import kotlinx.android.synthetic.main.dialog_assign_andon.view.*
import kotlinx.android.synthetic.main.dialog_detail_andon.*
import kotlinx.android.synthetic.main.item_andon.view.*

private var tambaheskalasi: Int = 1

class AndonAdapter(val dataAndon: List<DataAndon>?) :
    RecyclerView.Adapter<AndonAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtstation = itemView.head
        val txtreason = itemView.reason
        val txtTime = itemView.time
        val eskalasi = itemView.progressBar
        val btnAssign = itemView.assign
        val txtdone = itemView.status
        val cardAndon = itemView.cardnotifgeneral
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_andon, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val stationname = dataAndon?.get(position)?.stationNum.toString()
        val reason = dataAndon?.get(position)?.name
        val startTime = dataAndon?.get(position)?.startTime
        val eskal = dataAndon?.get(position)?.escalation
        val status = dataAndon?.get(position)?.status
        val finishTime = dataAndon?.get(position)?.finishTime
        val deskripsiandon = dataAndon?.get(position)?.description

        holder.cardAndon.setOnClickListener {
            val dialog = Dialog(it.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_detail_andon)

            dialog.stationandon.text = "Station $stationname"
            if (reason == null) {
                dialog.reasonandon.text = "Penyebab : -"
            } else {
                dialog.reasonandon.text = "Penyebab : $reason"
            }

            if (finishTime == null) {
                dialog.finishtime.text = "-"
            } else {
                dialog.finishtime.text = finishTime
            }

            if (startTime == null) {
                dialog.starttime.text = "-"
            } else {
                dialog.starttime.text = startTime
            }

            if (deskripsiandon == null) {
                dialog.deskripsi.text = "Tidak Ada"
            } else {
                dialog.deskripsi.text = deskripsiandon.toString()
            }

            dialog.setCancelable(true)
            dialog.btnOKAndon.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
        holder.txtstation.text = "Station $stationname Mengalami Downtime"
        holder.txtreason.text = "Penyebab : $reason"
        holder.txtTime.text = startTime
        if (eskal != null) {
            holder.eskalasi.curValue = eskal
        } else {
            holder.eskalasi.curValue = 1
        }

        if (reason == null) {
            holder.txtreason.text = "Penyebab : -"
        } else {
            holder.txtreason.text = "Penyebab : $reason"
        }


        if (status == 0) {
            holder.txtdone.visibility = View.GONE
            holder.btnAssign.visibility = View.VISIBLE
            holder.btnAssign.setOnClickListener {
                val dialog = Dialog(it.context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setContentView(R.layout.dialog_assign_andon)
                dialog.btnOK.setOnClickListener {
                    holder.eskalasi.curValue = tambaheskalasi++
                    dialog.dismiss()
                    holder.txtdone.text = "DONE"
                    Toast.makeText(
                        it.context, "Berhasil", Toast.LENGTH_SHORT
                    ).show()
                }
                dialog.btnBatal.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }
        } else {
            holder.txtdone.visibility = View.VISIBLE
            holder.btnAssign.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return dataAndon!!.size
    }

}


