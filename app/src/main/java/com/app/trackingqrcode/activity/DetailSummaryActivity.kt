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
import kotlinx.android.synthetic.main.activity_detail_part.*
import kotlinx.android.synthetic.main.activity_detail_summary.*
import kotlinx.android.synthetic.main.activity_detail_summary.Pavail
import kotlinx.android.synthetic.main.activity_detail_summary.Pperform
import kotlinx.android.synthetic.main.activity_detail_summary.Vefficiency
import kotlinx.android.synthetic.main.activity_detail_summary.Voee
import kotlinx.android.synthetic.main.activity_detail_summary.backSum
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.ceil
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

        showdetailsummary()
        hide()
        animation()

        backSum.setOnClickListener {
            startActivity(Intent(this, SummaryActivity::class.java))
        }
    }

    private fun showdetailsummary() {
        idstation = intent.getStringExtra(KEY_STATION).toString()
        idproduct = intent.getStringExtra(KEY_PRODUK).toString()
        val retro = ApiUtils().getUserService()
        retro.getDetailSummary(idstation,idproduct).enqueue(object : Callback<DetailSummaryResponse>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<DetailSummaryResponse>, response: Response<DetailSummaryResponse>) {
                val detailsummary = response.body()
                if (detailsummary!=null && detailsummary.success == true && detailsummary.data != null) {
                    Toast.makeText(applicationContext, "Load Berhasil", Toast.LENGTH_LONG).show()
                    tvstation.text = intent.getStringExtra(STATION).toString()
                    tvcust.text = intent.getStringExtra(CUSTOMER).toString()

                    tv1.text = detailsummary.data.sku
                    tv2.text = detailsummary.data.partName
                    tv3.text = detailsummary.data.operatorName
                    tv4.text = detailsummary.data.startTime
                    tv5.text = detailsummary.data.finishTime
                    tv6.text = detailsummary.data.cycleTime.toString()
                    tv7.text = detailsummary.data.target.toString()

                    val actual = detailsummary.data.actual
                    val target = detailsummary.data.target
                    val reject = detailsummary.data.rejection
                    val avail = detailsummary.data.avaibility
                    val perform = detailsummary.data.performance
                    val efficiency = detailsummary.data.efficiency
                    val oee = detailsummary.data.oee
                    val downtime = detailsummary.data.downtime

                    if(efficiency != null && efficiency != 0){
                        Vefficiency.text = "$efficiency%"
                    }else{
                        Vefficiency.text = "0%"
                    }

                    if(oee != null && oee != 0){
                        Voee.text = "$oee%"
                    }else{
                        Voee.text = "0%"
                    }

                    if(avail != null && avail != 0){
                        Pavail.text = "$avail%"
                    }else{
                        Pavail.text = "0%"
                    }

                    if(perform != null && perform != 0){
                        Pperform.text = "$perform%"
                    }else{
                        Pperform.text = "0%"
                    }

                    if (downtime!=null && downtime!=0){
                        val downtimeps = downtime.toFloat()
                        val downtimepm = ceil(downtimeps.div(60)).toInt().toString()
                        Vdowntime.text = "$downtimepm Menit"
                    }else{
                        Vdowntime.text = "0 Menit"
                    }

                    if (target!=null && actual!=null && reject!=null){
                        val targetFloat = target.toFloat()
                        val actualFloat = actual.toFloat()
                        val rejectFloat = reject.toFloat()
                        val targetpersen = 100.div(targetFloat).times(targetFloat).toInt()
                        val actualpersen = 100.div(targetFloat).times(actualFloat).toInt()
                        val okratio = (actualFloat.div(actualFloat.plus(rejectFloat))).times(100)
                        val achievement = (actualFloat.div(targetFloat)).times(100)
                        val rejection = (rejectFloat.div((actualFloat.plus(rejectFloat))).times(100))

                        if(!okratio.isNaN() && !achievement.isNaN() && !rejection.isNaN()){
                            val number3digits = (rejection * 1000.0).roundToInt() / 1000.0
                            val number2digits = (number3digits * 100.0).roundToInt() / 100.0
                            val rejectdec = (number2digits * 10.0).roundToInt() / 10.0

                            PbTarget.progressTintList = ColorStateList.valueOf(Color.GREEN)
                            PbTarget.progress = targetpersen

                            if (okratio.roundToInt() <70){
                                PbOk.progressTintList = ColorStateList.valueOf(Color.RED)
                                PbOk.progress = okratio.roundToInt()
                            }else if(okratio.roundToInt() in 70..80){
                                PbOk.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                                PbOk.progress = okratio.roundToInt()
                            }else{
                                PbOk.progressTintList = ColorStateList.valueOf(Color.GREEN)
                                PbOk.progress = okratio.roundToInt()
                            }

                            Vachievemen.text = "${achievement.toInt()}%"
                            Vrejections.text = "$rejectdec%"
                            Pokratioo.text = "${okratio.toInt()}%"
                            Ptarget.text = target.toString()
                            Pactual.text = actual.toString()

                            if (actualpersen <70){
                                PbAct.progressTintList = ColorStateList.valueOf(Color.RED)
                                PbAct.progress = actualpersen
                            }else if(actualpersen in 70..80){
                                PbAct.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                                PbAct.progress = actualpersen
                            }else{
                                PbAct.progressTintList = ColorStateList.valueOf(Color.GREEN)
                                PbAct.progress = actualpersen
                            }
                        }else{
                            Vachievemen.text = "0%"
                            Vrejections.text = "0"
                            Pokratioo.text = "0%"
                            Ptarget.text = "0"
                            Pactual.text = "0"
                        }
                    }else{
                        Vachievemen.text = "0%"
                        Vrejections.text = "0"
                        Pokratioo.text = "0%"
                        Ptarget.text = "0"
                        Pactual.text = "0"
                    }

                    if (avail != null) {
                        if (avail < 70){
                            PbAvail.progressTintList = ColorStateList.valueOf(Color.RED)
                            PbAvail.progress = avail
                        }else if(avail in 70..80){
                            PbAvail.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                            PbAvail.progress = avail
                        }else{
                            PbAvail.progressTintList = ColorStateList.valueOf(Color.GREEN)
                            PbAvail.progress = avail
                        }
                    }else{
                        PbAvail.progressTintList = ColorStateList.valueOf(Color.RED)
                        PbAvail.progress = 0
                    }

                    if (perform != null) {
                        if (perform <70){
                            PbPerform.progressTintList = ColorStateList.valueOf(Color.RED)
                            PbPerform.progress = perform
                        }else if(perform in 70..80){
                            PbPerform.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                            PbPerform.progress = perform
                        }else{
                            PbPerform.progressTintList = ColorStateList.valueOf(Color.GREEN)
                            PbPerform.progress = perform
                        }
                    }else{
                        PbPerform.progressTintList = ColorStateList.valueOf(Color.RED)
                        PbPerform.progress = 0
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
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, SummaryActivity::class.java))
    }
}