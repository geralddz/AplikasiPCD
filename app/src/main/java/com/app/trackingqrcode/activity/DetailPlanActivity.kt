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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.app.trackingqrcode.R
import com.app.trackingqrcode.api.ApiUtils
import com.app.trackingqrcode.api.SharedPref
import com.app.trackingqrcode.response.DetailPlanResponse
import com.app.trackingqrcode.socket.ListenDataSocket
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_detail_part.*
import kotlinx.android.synthetic.main.activity_detail_part.Pavail
import kotlinx.android.synthetic.main.activity_detail_part.Pokratio
import kotlinx.android.synthetic.main.activity_detail_part.Pperform
import kotlinx.android.synthetic.main.activity_detail_part.Vachievement
import kotlinx.android.synthetic.main.activity_detail_part.Vefficiency
import kotlinx.android.synthetic.main.activity_detail_part.Voee
import kotlinx.android.synthetic.main.activity_detail_part.Vrejection
import kotlinx.android.synthetic.main.activity_detail_part.backSum
import kotlinx.android.synthetic.main.activity_detail_summary.*
import net.mrbin99.laravelechoandroid.Echo
import net.mrbin99.laravelechoandroid.EchoOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.ceil
import kotlin.math.roundToInt

class DetailPlanActivity : AppCompatActivity() {
    private lateinit var id_plan: String
    private lateinit var id_station: String
    private lateinit var sharedPref: SharedPref
    private lateinit var status: String
    private lateinit var stationname: String
    private lateinit var partname: String
    private var rotate: Animation? = null
    private var rotateup: Animation? = null
    private var _receivedEvent = MutableLiveData<Any>()
    private var receivedEvent = _receivedEvent
    private var echo: Echo? = null

    companion object {
        const val PartName = "partname"
        const val SKU = "sku"
        const val Startprod = "start"
        const val LastFinish = "finish"
        const val SPH = "SPH"
        const val TARGET = "TARGET"
        const val KEY_PLAN = "id_plan"
        const val SHIFT = "shift"
        const val SERVER_URL = "http://10.14.130.94:6001"
        const val CHANNEL_MESSAGES = "dashboard"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_part)
        sharedPref = SharedPref(this)
        backSum.setOnClickListener {
            startActivity(Intent(this,DetailStationActivity::class.java))
        }
        connectToSocket()
        initLiveDataListener()
        id_station = sharedPref.getIdStation().toString()
        status = sharedPref.getStatus().toString()
        stationname = sharedPref.getStation().toString()
        partname = sharedPref.getPartname().toString()
        id_plan = intent.getStringExtra(KEY_PLAN).toString()
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate)
        rotateup = AnimationUtils.loadAnimation(this, R.anim.rotateup)
        shiftStation.text = intent.getStringExtra(SHIFT).toString()
        namaStation.text = stationname
        statusStation.text = status
        tvsku.text = intent.getStringExtra(SKU).toString()
        tvpn.text = intent.getStringExtra(PartName).toString()
        tvstart.text = intent.getStringExtra(Startprod).toString()
        tvfinish.text = intent.getStringExtra(LastFinish).toString()
        tvsph.text = intent.getStringExtra(SPH).toString()
        tvtarget.text = intent.getStringExtra(TARGET).toString()
        showDetailPlan()
        hide()
        animation()

    }

    private fun showDetailPlan() {
        val retro = ApiUtils().getUserService()
        retro.getDetailPlan(id_plan).enqueue(object : Callback<DetailPlanResponse>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<DetailPlanResponse>, response: Response<DetailPlanResponse>) {
                val detailplan = response.body()
                Log.e("plan", "onResponse: $id_plan")
                if (detailplan!=null){
                    val target = detailplan.cumTarget
                    val actual = detailplan.actual
                    val reject = detailplan.rejection
                    val avail = detailplan.avaibility
                    val perform = detailplan.performance
                    val downtime = detailplan.downtime
                    val efficiency = detailplan.efficiency
                    val oee = detailplan.oee
                    val operator = detailplan.userId

                    if (downtime!=null || downtime!=0){
                        val downtimeps = downtime?.toFloat()
                        val downtimepm = ceil(downtimeps?.div(60)!!)
                        Vdowntim.text = downtimepm.toInt().toString()+" Menit"
                    }else{
                        Vdowntim.text = "0 Menit"
                    }

                    if (target!=null && actual!=null && reject!=null){
                        val targetFloat = target.toFloat()
                        val actualFloat = actual.toFloat()
                        val rejectFloat = reject.toFloat()
                        val targetpersen = 100.div(targetFloat).times(targetFloat).toInt()
                        val actualpersen = 100.div(targetFloat).times(actualFloat).toInt()
                        val okratio = (actualFloat.div(actualFloat.plus(rejectFloat))).div(100).toInt()
                        val achievement = (actualFloat.div(targetFloat)).times(100).toInt()
                        val rejection = ((rejectFloat.div((actualFloat.plus(rejectFloat)))).times(100)).toInt()
                        Pokratio.text = "$okratio%"
                        Vachievement.text = "$achievement%"
                        Vrejection.text = "$rejection"

                        if (okratio <70){
                            POk.progressTintList = ColorStateList.valueOf(Color.RED)
                            POk.progress = okratio
                        }else if(okratio in 70..80){
                            POk.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                            POk.progress = okratio
                        }else{
                            POk.progressTintList = ColorStateList.valueOf(Color.GREEN)
                            POk.progress = okratio
                        }

                        PTarget.progressTintList = ColorStateList.valueOf(Color.GREEN)
                        PTarget.progress = targetpersen

                        if (actualpersen <70){
                            PAct.progressTintList = ColorStateList.valueOf(Color.RED)
                            PAct.progress = actualpersen
                        }else if(actualpersen in 70..80){
                            PAct.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                            PAct.progress = actualpersen
                        }else{
                            PAct.progressTintList = ColorStateList.valueOf(Color.GREEN)
                            PAct.progress = actualpersen
                        }
                    }

                    if (target!=0 && actual!=0 && reject!=0){
                        val targetFloat = target?.toFloat()
                        val actualFloat = actual?.toFloat()
                        val rejectFloat = reject?.toFloat()
                        val targetpersen = 100.div(targetFloat!!).times(targetFloat).toInt()
                        val actualpersen = 100.div(targetFloat).times(actualFloat!!).toInt()
                        val okratio = (actualFloat.div(actualFloat.plus(rejectFloat!!))).div(100).roundToInt()
                        val achievement = (actualFloat.div(targetFloat)).times(100).roundToInt()
                        val rejection = ((rejectFloat.div((actualFloat.plus(rejectFloat)))).times(100)).roundToInt()

                        Pokratio.text = "$okratio%"
                        Vachievement.text = "$achievement%"
                        Vrejection.text = "$rejection"

                        if (okratio <70){
                            POk.progressTintList = ColorStateList.valueOf(Color.RED)
                            POk.progress = okratio
                        }else if(okratio in 70..80){
                            POk.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                            POk.progress = okratio
                        }else{
                            POk.progressTintList = ColorStateList.valueOf(Color.GREEN)
                            POk.progress = okratio
                        }

                        PTarget.progressTintList = ColorStateList.valueOf(Color.GREEN)
                        PTarget.progress = targetpersen

                        if (actualpersen <70){
                            PAct.progressTintList = ColorStateList.valueOf(Color.RED)
                            PAct.progress = actualpersen
                        }else if(actualpersen in 70..80){
                            PAct.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                            PAct.progress = actualpersen
                        }else{
                            PAct.progressTintList = ColorStateList.valueOf(Color.GREEN)
                            PAct.progress = actualpersen
                        }
                    }

                    Vefficiency.text = efficiency.toString()+"%"
                    Voee.text = oee.toString()+"%"
                    Pavail.text = avail.toString()+"%"
                    Pperform.text = perform.toString()+"%"
                    ptarget.text = target.toString()
                    pactual.text = actual.toString()
                    tvop.text = operator.toString()

                    if (avail != null) {
                        if (avail < 70){
                            PAvail.progressTintList = ColorStateList.valueOf(Color.RED)
                            PAvail.progress = avail
                        }else if(avail in 70..80){
                            PAvail.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                            PAvail.progress = avail
                        }else{
                            PAvail.progressTintList = ColorStateList.valueOf(Color.GREEN)
                            PAvail.progress = avail
                        }
                    }

                    if (perform != null) {
                        if (perform <70){
                            PPerform.progressTintList = ColorStateList.valueOf(Color.RED)
                            PPerform.progress = perform
                        }else if(perform in 70..80){
                            PPerform.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                            PPerform.progress = perform
                        }else{
                            PPerform.progressTintList = ColorStateList.valueOf(Color.GREEN)
                            PPerform.progress = perform
                        }
                    }
                }
            }
            override fun onFailure(call: Call<DetailPlanResponse>, t: Throwable) {
                Log.e("Error", t.message!!)
            }

        })
    }

    private fun animation(){
        btexpandoee.setOnClickListener {
            if (Layoutoee.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(cardOee, AutoTransition())
                Layoutoee.visibility = View.VISIBLE
                btexpandoee.startAnimation(rotateup)
            } else {
                TransitionManager.beginDelayedTransition(cardOee, AutoTransition())
                Layoutoee.visibility = View.GONE
                btexpandoee.startAnimation(rotate)
            }
        }
        btexpandach.setOnClickListener {
            if (Layoutach.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(cardAch, AutoTransition())
                Layoutach.visibility = View.VISIBLE
                btexpandach.startAnimation(rotateup)
            } else {
                TransitionManager.beginDelayedTransition(cardAch, AutoTransition())
                Layoutach.visibility = View.GONE
                btexpandach.startAnimation(rotate)
            }
        }
        btexpanddown.setOnClickListener {
            if (Layoutdown.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(cardDown, AutoTransition())
                Layoutdown.visibility = View.VISIBLE
                btexpanddown.startAnimation(rotateup)
            } else {
                TransitionManager.beginDelayedTransition(cardDown, AutoTransition())
                Layoutdown.visibility = View.GONE
                btexpanddown.startAnimation(rotate)
            }
        }
        btexpandreject.setOnClickListener {
            if (Layoutreject.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(cardReject, AutoTransition())
                Layoutreject.visibility = View.VISIBLE
                btexpandreject.startAnimation(rotateup)
            } else {
                TransitionManager.beginDelayedTransition(cardReject, AutoTransition())
                Layoutreject.visibility = View.GONE
                btexpandreject.startAnimation(rotate)
            }
        }
    }

    private fun hide() {
        Layoutoee.visibility = View.GONE
        Layoutach.visibility = View.GONE
        Layoutdown.visibility = View.GONE
        Layoutreject.visibility = View.GONE
    }
    private fun connectToSocket() {
        val options = EchoOptions()
        options.host = SERVER_URL
        options.eventNamespace = ""
        echo = Echo(options)
        echo?.connect({
            Log.d("socket","successful connect")
            listenForEvents()
        }
        ) { args -> Log.e("socket","error while connecting: $args") }
    }

    private fun displayNewEvent(event: ListenDataSocket?) {
        Log.e("value", "new event $event")
        _receivedEvent.postValue(event)
    }

    private fun listenForEvents() {
        echo?.let { it ->
            it.channel(CHANNEL_MESSAGES)
                .listen("Achievement_$id_station") {
                    val data = ListenDataSocket.showdata(it)
                    displayNewEvent(data)
                    Log.e("event baru", "$data")

                }
        }
    }

    private fun initLiveDataListener() {
        receivedEvent.observe(this) {
            if (it != null) {
                displayEventData(it)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayEventData(event: Any) {
        if (event is ListenDataSocket) {
            val temp = event.temp_achievement[0]
            val temp1 = Gson().toJson(temp)
            val objec = Gson().fromJson(temp1, DetailPlanResponse::class.java)
            val user = objec.userId
            val down = objec.downtime
            val eff = objec.efficiency
            val avail = objec.avaibility
            val perform = objec.performance
            val actual = objec.actual
            val target = objec.cumTarget
            val oee = objec.oee
            val reject = objec.rejection
            val rejectFloat = reject?.toFloat()
            val targetFloat = target?.toFloat()
            val actualFloat = actual?.toFloat()
            val targetpersen = 100.div(targetFloat!!).times(targetFloat).toInt()
            val actualpersen = 100.div(targetFloat).times(actualFloat!!).toInt()
            val okratio = (actualFloat.div(actualFloat.plus(rejectFloat!!))).div(100).toInt()
            val achievement = (actualFloat.div(targetFloat)).times(100).roundToInt()
            val rejection = ((rejectFloat.div((actualFloat.plus(rejectFloat)))).times(100)).toInt()

            Pokratio.text = "$okratio%"
            Voee.text = "$oee%"
            Pavail.text = "$avail%"
            Pperform.text = "$perform%"
            ptarget.text = target.toString()
            pactual.text = actual.toString()
            Vefficiency.text = "$eff%"
            tvop.text = user.toString()

            Pokratio.text = "$okratio%"
            Vachievement.text = "$achievement%"
            Vrejection.text = "$rejection"

            if (okratio <70){
                POk.progressTintList = ColorStateList.valueOf(Color.RED)
                POk.progress = okratio
            }else if(okratio in 70..80){
                POk.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                POk.progress = okratio
            }else{
                POk.progressTintList = ColorStateList.valueOf(Color.GREEN)
                POk.progress = okratio
            }

            PTarget.progressTintList = ColorStateList.valueOf(Color.GREEN)
            PTarget.progress = targetpersen

            if (actualpersen <70){
                PAct.progressTintList = ColorStateList.valueOf(Color.RED)
                PAct.progress = actualpersen
            }else if(actualpersen in 70..80){
                PAct.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                PAct.progress = actualpersen
            }else{
                PAct.progressTintList = ColorStateList.valueOf(Color.GREEN)
                PAct.progress = actualpersen
            }

            if (avail != null) {
                if (avail < 70){
                    PAvail.progressTintList = ColorStateList.valueOf(Color.RED)
                    PAvail.progress = avail
                }else if(avail in 70..80){
                    PAvail.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                    PAvail.progress = avail
                }else{
                    PAvail.progressTintList = ColorStateList.valueOf(Color.GREEN)
                    PAvail.progress = avail
                }
            }

            if (perform != null) {
                if (perform <70){
                    PPerform.progressTintList = ColorStateList.valueOf(Color.RED)
                    PPerform.progress = perform
                }else if(perform in 70..80){
                    PPerform.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                    PPerform.progress = perform
                }else{
                    PPerform.progressTintList = ColorStateList.valueOf(Color.GREEN)
                    PPerform.progress = perform
                }
            }

            if (down!=null || down!=0){
                val downtimeps = down?.toFloat()
                val downtimepm = ceil(downtimeps?.div(60)!!)
                Vdowntim.text = downtimepm.toInt().toString()+" Menit"
            }else{
                Vdowntim.text = "0 Menit"
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        echo?.disconnect()
    }
}