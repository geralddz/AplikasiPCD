package com.app.trackingqrcode.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.trackingqrcode.R
import com.app.trackingqrcode.api.ApiUtils
import com.app.trackingqrcode.response.DetailSummaryResponse
import kotlinx.android.synthetic.main.activity_detail_summary.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt


class DetailSummaryActivity : AppCompatActivity() {
    private lateinit var idstation : String
    private lateinit var idproduct : String
    private var rotate: Animation? = null
    private var rotateup: Animation? = null

    companion object {
        private const val KEY_STATION = "id_station"
        private const val KEY_PRODUK = "id_produk"
        private const val STATION = "station"
        private const val CUSTOMER = "customer"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_summary)

        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate)
        rotateup = AnimationUtils.loadAnimation(this, R.anim.rotateup)
        idstation = intent.getStringExtra(KEY_STATION).toString()
        idproduct = intent.getStringExtra(KEY_PRODUK).toString()
        showdetailsummary(idstation,idproduct)
        hide()
        animation()

        backSum.setOnClickListener {
            startActivity(Intent(this, SummaryActivity::class.java))
        }
    }

    private fun showdetailsummary(id_station:String,id_product:String) {
        val retro = ApiUtils().getUserService()
        retro.getDetailSummary(id_station,id_product).enqueue(object : Callback<DetailSummaryResponse>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<DetailSummaryResponse>, response: Response<DetailSummaryResponse>) {
                val detailsummary = response.body()
                if (detailsummary!=null && detailsummary.success == true && detailsummary.data != null) {
                    Toast.makeText(applicationContext, "Load Berhasil", Toast.LENGTH_LONG).show()
                    tvstation.text = intent.getStringExtra(STATION).toString()
                    tvcust.text = intent.getStringExtra(CUSTOMER).toString()
                    val actual = detailsummary.data.actual?.toFloat()
                    val target = detailsummary.data.target?.toFloat()
                    val reject = detailsummary.data.rejection?.toFloat()
                    val avail = detailsummary.data.avaibility
                    val perform = detailsummary.data.performance
                    val achievement = ((actual!!/(target!!))*100).roundToInt()
                    val okratio = ((actual / (actual + reject!!))*100).roundToInt()
                    val rejection = ((reject / (actual + reject))*100).roundToInt()
                    tv1.text = detailsummary.data.sku
                    tv2.text = detailsummary.data.partName
                    tv3.text = detailsummary.data.operatorName
                    tv4.text = detailsummary.data.startTime
                    tv5.text = detailsummary.data.finishTime
                    tv6.text = detailsummary.data.cycleTime.toString()
                    tv7.text = detailsummary.data.target.toString()
                    Vefficiency.text = "${detailsummary.data.efficiency.toString()}%"
                    Voee.text = "${detailsummary.data.oee.toString()}%"
                    Vachievement.text = "$achievement%"
                    Vrejection.text = "$rejection%"
                    Vdowntime.text = "${detailsummary.data.downtime.toString()}%"
                    Pactual.text = detailsummary.data.actual.toString()
                    Ptarget.text = detailsummary.data.target.toString()
                    Pavail.text = "${detailsummary.data.avaibility.toString()}%"
                    Pperform.text = "${detailsummary.data.performance.toString()}%"
                    Pokratio.text = "$okratio%"

                    if (avail!! < 70){
                        PbAvail.progressTintList = ColorStateList.valueOf(Color.RED)
                        PbAvail.progress = avail
                    }else if(avail in 70..80){
                        PbAvail.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                        PbAvail.progress = avail
                    }else{
                        PbAvail.progressTintList = ColorStateList.valueOf(Color.GREEN)
                        PbAvail.progress = avail
                    }

                    if (okratio <70){
                        PbOk.progressTintList = ColorStateList.valueOf(Color.RED)
                        PbOk.progress = okratio
                    }else if(okratio in 70..80){
                        PbOk.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                        PbOk.progress = okratio
                    }else{
                        PbOk.progressTintList = ColorStateList.valueOf(Color.GREEN)
                        PbOk.progress = okratio
                    }

                    if (perform!! <70){
                        PbPerform.progressTintList = ColorStateList.valueOf(Color.RED)
                        PbPerform.progress = perform
                    }else if(perform in 70..80){
                        PbPerform.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                        PbPerform.progress = perform
                    }else{
                        PbPerform.progressTintList = ColorStateList.valueOf(Color.GREEN)
                        PbPerform.progress = perform
                    }

                    PbTarget.progressTintList = ColorStateList.valueOf(Color.GREEN)
                    PbTarget.progress = target.toInt()

                    if (actual <70){
                        PbAct.progressTintList = ColorStateList.valueOf(Color.RED)
                        PbAct.progress = achievement
                    }else if(actual.toInt() in 70..80){
                        PbAct.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                        PbAct.progress = achievement
                    }else{
                        PbAct.progressTintList = ColorStateList.valueOf(Color.GREEN)
                        PbAct.progress = achievement
                    }


                }else{
                    Toast.makeText(applicationContext, "Data Detail Summary Tidak Ada", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<DetailSummaryResponse>, t: Throwable) {
                Log.e("Error", t.message!!)
            }
        })
    }

    private fun animation(){
        btnexpandoee.setOnClickListener {
            if (LayoutOee.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(CardOee, AutoTransition())
                LayoutOee.visibility = View.VISIBLE
                btnexpandoee.startAnimation(rotateup)
            } else {
                TransitionManager.beginDelayedTransition(CardOee, AutoTransition())
                LayoutOee.visibility = View.GONE
                btnexpandoee.startAnimation(rotate)
            }
        }
        btnexpandach.setOnClickListener {
            if (LayoutAch.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(CardAch, AutoTransition())
                LayoutAch.visibility = View.VISIBLE
                btnexpandach.startAnimation(rotateup)
            } else {
                TransitionManager.beginDelayedTransition(CardAch, AutoTransition())
                LayoutAch.visibility = View.GONE
                btnexpandach.startAnimation(rotate)
            }
        }
        btnexpanddown.setOnClickListener {
            if (LayoutDown.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(CardDown, AutoTransition())
                LayoutDown.visibility = View.VISIBLE
                btnexpanddown.startAnimation(rotateup)
            } else {
                TransitionManager.beginDelayedTransition(CardDown, AutoTransition())
                LayoutDown.visibility = View.GONE
                btnexpanddown.startAnimation(rotate)
            }
        }
        btnexpandreject.setOnClickListener {
            if (LayoutReject.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(CardReject, AutoTransition())
                LayoutReject.visibility = View.VISIBLE
                btnexpandreject.startAnimation(rotateup)
            } else {
                TransitionManager.beginDelayedTransition(CardReject, AutoTransition())
                LayoutReject.visibility = View.GONE
                btnexpandreject.startAnimation(rotate)
            }
        }
    }

    private fun hide() {
        LayoutOee.visibility = View.GONE
        LayoutAch.visibility = View.GONE
        LayoutDown.visibility = View.GONE
        LayoutReject.visibility = View.GONE
    }
}