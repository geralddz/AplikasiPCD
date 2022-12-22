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
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.trackingqrcode.R
import com.app.trackingqrcode.adapter.DowntimePlanAdapter
import com.app.trackingqrcode.adapter.RejectionPlanAdapter
import com.app.trackingqrcode.response.DetailPlanResponse
import com.app.trackingqrcode.response.DowntimeResponse
import com.app.trackingqrcode.response.RejectionPlanResponse
import com.app.trackingqrcode.utils.ApiUtils
import com.app.trackingqrcode.utils.SharedPref
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_detail_part.*
import kotlinx.android.synthetic.main.activity_detail_part.Pavail
import kotlinx.android.synthetic.main.activity_detail_part.Pperform
import kotlinx.android.synthetic.main.activity_detail_part.Voee
import kotlinx.android.synthetic.main.activity_detail_part.backSum
import kotlinx.android.synthetic.main.activity_detail_part.cardAch
import kotlinx.android.synthetic.main.activity_detail_part.cardOee
import kotlinx.android.synthetic.main.activity_detail_part.namaStation
import kotlinx.android.synthetic.main.activity_detail_part.statusStation
import kotlinx.android.synthetic.main.activity_detail_station.*
import kotlinx.android.synthetic.main.activity_detail_summary.*
import kotlinx.android.synthetic.main.activity_scan.*
import net.mrbin99.laravelechoandroid.Echo
import net.mrbin99.laravelechoandroid.EchoOptions
import org.json.JSONObject
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
    private lateinit var downtime : String
    private lateinit var partname: String
    private var rotate: Animation? = null
    private var rotateup: Animation? = null
    private var echo: Echo? = null
    private val CHANNEL_MESSAGES = "dashboard"

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_part)
        sharedPref = SharedPref(this)
        backSum.setOnClickListener {
            startActivity(Intent(this, DetailStationActivity::class.java))
        }
        downtime = sharedPref.getIsDowntime().toString()
        id_station = sharedPref.getIdStation().toString()
        status = sharedPref.getStatus().toString()
        stationname = sharedPref.getStation().toString()
        partname = sharedPref.getPartname().toString()
        id_plan = intent.getStringExtra(KEY_PLAN).toString()
        rotate = AnimationUtils.loadAnimation(this, R.anim.rotate)
        rotateup = AnimationUtils.loadAnimation(this, R.anim.rotateup)
        shiftStation.text = intent.getStringExtra(SHIFT).toString()
        namaStation.text = stationname
        tvsku.text = intent.getStringExtra(SKU).toString()
        tvpn.text = intent.getStringExtra(PartName).toString()
        tvstart.text = intent.getStringExtra(Startprod).toString()
        tvfinish.text = intent.getStringExtra(LastFinish).toString()
        tvsph.text = intent.getStringExtra(SPH).toString()
        tvtarget.text = intent.getStringExtra(TARGET).toString()

        connectToSocket()
        showDetailPlan()
        hide()
        animation()
        coloring()

    }

    private fun coloring() {
        if (status == "Start") {
            if (downtime == "1") {
                cvplan.setCardBackgroundColor(Color.YELLOW)
                statusStation.text = "Problem"
            } else {
                cvplan.setCardBackgroundColor(Color.GREEN)
                statusStation.text = "Start"
            }
        } else {
            statusStation.text = "Stop"
            cvplan.setCardBackgroundColor(Color.RED)
            statusStation.setTextColor(Color.WHITE)
            shiftStation.setTextColor(Color.WHITE)
            namaStation.setTextColor(Color.WHITE)
        }
    }

    private fun showDetailPlan() {
        val retro = ApiUtils().getUserService()
        retro.getRejectionPlan(id_plan,id_station).enqueue(object : Callback<RejectionPlanResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<RejectionPlanResponse>, response: Response<RejectionPlanResponse>) {
                val rejectplan = response.body()
                val dataRejectionPlan = rejectplan?.data
                if (rejectplan?.success == true){
                    val rejectionPlanAdapter = RejectionPlanAdapter(dataRejectionPlan)

                    rvrejectionplan.apply {
                        layoutManager = LinearLayoutManager(this@DetailPlanActivity)
                        setHasFixedSize(true)
                        adapter = rejectionPlanAdapter
                        rejectionPlanAdapter.notifyDataSetChanged()
                    }
                }
            }
            override fun onFailure(call: Call<RejectionPlanResponse>, t: Throwable) {
                Log.e("Error", t.message!!)
            }
        })

        retro.getDowntime(id_station ,id_plan).enqueue(object : Callback<DowntimeResponse>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<DowntimeResponse>, response: Response<DowntimeResponse>) {
                val downtime = response.body()
                val datadowntime = downtime?.data
                if (downtime?.success == true){
                    val downtimePlanAdapter = DowntimePlanAdapter(datadowntime)
                    rvdown.apply {
                        layoutManager = LinearLayoutManager(this@DetailPlanActivity)
                        setHasFixedSize(true)
                        adapter = downtimePlanAdapter
                        downtimePlanAdapter.notifyDataSetChanged()
                    }
                }
            }
            override fun onFailure(call: Call<DowntimeResponse>, t: Throwable) {
                Log.e("Error", t.message!!)
            }
        })

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
                    val operator = detailplan.operator

                    if(operator!=null){
                        tvop.text = operator.toString()
                    }else{
                        tvop.text = ""
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
                        Vdowntim.text = "$downtimepm Menit"
                    }else{
                        Vdowntim.text = "0 Menit"
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

                            if (avail!=null&&perform!=null){
                                val oee = (avail.times(perform).times(okratio)).div(10000).toInt()
                                Voee.text ="$oee%"
                            }
                            PTarget.progressTintList = ColorStateList.valueOf(Color.GREEN)
                            PTarget.progress = targetpersen

                            if (okratio.toInt() <70){
                                POk.progressTintList = ColorStateList.valueOf(Color.RED)
                                POk.progress = okratio.toInt()
                            }else if(okratio.toInt() in 70..90){
                                POk.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                                POk.progress = okratio.toInt()
                            }else{
                                POk.progressTintList = ColorStateList.valueOf(Color.GREEN)
                                POk.progress = okratio.toInt()
                            }

                            Vachievement.text = "${achievement.toInt()}%"
                            Vrejection.text = "$rejectdec%"
                            Pokratio.text = "${okratio.toInt()}%"
                            ptarget.text = target.toString()
                            pactual.text = actual.toString()

                            if (actualpersen <70){
                                PAct.progressTintList = ColorStateList.valueOf(Color.RED)
                                PAct.progress = actualpersen
                            }else if(actualpersen in 70..90){
                                PAct.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                                PAct.progress = actualpersen
                            }else{
                                PAct.progressTintList = ColorStateList.valueOf(Color.GREEN)
                                PAct.progress = actualpersen
                            }
                        }else{
                            Vachievement.text = "0%"
                            Vrejection.text = "0"
                            Pokratio.text = "0%"
                            ptarget.text = "0"
                            pactual.text = "0"
                        }
                    }else{
                        Vachievement.text = "0%"
                        Vrejection.text = "0"
                        Pokratio.text = "0%"
                        ptarget.text = "0"
                        pactual.text = "0"
                    }

                    if (avail != null) {
                        if (avail < 70){
                            PAvail.progressTintList = ColorStateList.valueOf(Color.RED)
                            PAvail.progress = avail
                        }else if(avail in 70..90){
                            PAvail.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                            PAvail.progress = avail
                        }else{
                            PAvail.progressTintList = ColorStateList.valueOf(Color.GREEN)
                            PAvail.progress = avail
                        }
                    }else{
                        PAvail.progressTintList = ColorStateList.valueOf(Color.RED)
                        PAvail.progress = 0
                    }

                    if (perform != null) {
                        if (perform <70){
                            PPerform.progressTintList = ColorStateList.valueOf(Color.RED)
                            PPerform.progress = perform
                        }else if(perform in 70..90){
                            PPerform.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                            PPerform.progress = perform
                        }else{
                            PPerform.progressTintList = ColorStateList.valueOf(Color.GREEN)
                            PPerform.progress = perform
                        }
                    }else{
                        PPerform.progressTintList = ColorStateList.valueOf(Color.RED)
                        PPerform.progress = 0
                    }
                }
            }
            override fun onFailure(call: Call<DetailPlanResponse>, t: Throwable) {
                Log.e("Error", t.message!!)
            }
        })
    }

    private fun animation() {
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
            Log.d("socket", "successful connect")
            listenForEvents()
        }) { args -> Log.e("socket", "error while connecting: $args") }
    }

    private fun listenForEvents() {
        echo?.let { it ->
            it.channel(CHANNEL_MESSAGES)
                .listen("Achievement_$id_station") {
                    val data = showdata(it)
                    if (data != null) {
                        displayEventData(data)
                    }
                }
        }
    }

    private fun showdata(temp_achievement: Array<Any>): JSONObject?{
        val messageData = temp_achievement[1] as JSONObject
        Log.e("socketPlan", "data: $messageData")
        try {
            return messageData
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @SuppressLint("SetTextI18n")
    private fun displayEventData(event: JSONObject) {
        val temp = event.optJSONArray("temp_achievement")?.get(0).toString()
        val namauser = event.optJSONObject("user")?.get("name")
        val objec = Gson().fromJson(temp, DetailPlanResponse::class.java)
        val idplan = objec.planningId.toString()
        if (id_plan==idplan){
            runOnUiThread {
                tvop.text = namauser.toString()
                val down = objec.downtime
                val avail = objec?.avaibility
                val perform = objec.performance
                val actual = objec.actual
                val target = objec.cumTarget
                val reject = objec.rejection

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

                if (down!=null && down!=0){
                    val downtimeps = down.toFloat()
                    val downtimepm = ceil(downtimeps.div(60)).toInt().toString()
                    Vdowntim.text = "$downtimepm Menit"
                }else{
                    Vdowntim.text = "0 Menit"
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

                        if (avail!=null&&perform!=null){
                            val oee = (avail.times(perform).times(okratio)).div(10000).roundToInt()
                            if (oee!=0){
                                Voee.text = "$oee%"
                            }else {
                                Voee.text = "0%"
                            }
                        }

                        PTarget.progressTintList = ColorStateList.valueOf(Color.GREEN)
                        PTarget.progress = targetpersen

                        if (okratio.toInt() <70){
                            POk.progressTintList = ColorStateList.valueOf(Color.RED)
                            POk.progress = okratio.toInt()
                        }else if(okratio.toInt() in 70..90){
                            POk.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                            POk.progress = okratio.toInt()
                        }else{
                            POk.progressTintList = ColorStateList.valueOf(Color.GREEN)
                            POk.progress = okratio.toInt()
                        }

                        Vachievement.text = "${achievement.roundToInt()}%"
                        Vrejection.text = "$rejectdec%"
                        Pokratio.text = "${okratio.toInt()}%"
                        ptarget.text = target.toString()
                        pactual.text = actual.toString()

                        if (actualpersen <70){
                            PAct.progressTintList = ColorStateList.valueOf(Color.RED)
                            PAct.progress = actualpersen
                        }else if(actualpersen in 70..90){
                            PAct.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                            PAct.progress = actualpersen
                        }else{
                            PAct.progressTintList = ColorStateList.valueOf(Color.GREEN)
                            PAct.progress = actualpersen
                        }
                    }else{
                        Vachievement.text = "0%"
                        Vrejection.text = "0"
                        Pokratio.text = "0%"
                        ptarget.text = "0"
                        pactual.text = "0"
                    }
                }else{
                    Vachievement.text = "0%"
                    Vrejection.text = "0"
                    Pokratio.text = "0%"
                    ptarget.text = "0"
                    pactual.text = "0"
                }

                if (avail != null) {
                    if (avail < 70){
                        PAvail.progressTintList = ColorStateList.valueOf(Color.RED)
                        PAvail.progress = avail
                    }else if(avail in 70..90){
                        PAvail.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                        PAvail.progress = avail
                    }else{
                        PAvail.progressTintList = ColorStateList.valueOf(Color.GREEN)
                        PAvail.progress = avail
                    }
                }else{
                    PAvail.progressTintList = ColorStateList.valueOf(Color.RED)
                    PAvail.progress = 0
                }

                if (perform != null) {
                    if (perform <70){
                        PPerform.progressTintList = ColorStateList.valueOf(Color.RED)
                        PPerform.progress = perform
                    }else if(perform in 70..90){
                        PPerform.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                        PPerform.progress = perform
                    }else{
                        PPerform.progressTintList = ColorStateList.valueOf(Color.GREEN)
                        PPerform.progress = perform
                    }
                }else{
                    PPerform.progressTintList = ColorStateList.valueOf(Color.RED)
                    PPerform.progress = 0
                }
                Toast.makeText(applicationContext, "Update Data Planning Berhasil", Toast.LENGTH_LONG).show()
            }

            val retro = ApiUtils().getUserService()
            retro.getRejectionPlan(id_plan,id_station).enqueue(object : Callback<RejectionPlanResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<RejectionPlanResponse>, response: Response<RejectionPlanResponse>) {
                    val rejectplan = response.body()
                    val dataRejectionPlan = rejectplan?.data
                    if (rejectplan?.success == true){
                        val rejectionPlanAdapter = RejectionPlanAdapter(dataRejectionPlan)
                        rvrejectionplan.apply {
                            layoutManager = LinearLayoutManager(this@DetailPlanActivity)
                            setHasFixedSize(true)
                            adapter = rejectionPlanAdapter
                            rejectionPlanAdapter.notifyDataSetChanged()
                        }
                    }
                }
                override fun onFailure(call: Call<RejectionPlanResponse>, t: Throwable) {
                    Log.e("Error", t.message!!)
                }
            })

            retro.getDowntime(id_station,id_plan).enqueue(object : Callback<DowntimeResponse>{
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(call: Call<DowntimeResponse>, response: Response<DowntimeResponse>) {
                    val downtime = response.body()
                    val datadowntime = downtime?.data
                    if (downtime?.success == true){
                        val downtimePlanAdapter = DowntimePlanAdapter(datadowntime)
                        rvdown.apply {
                            layoutManager = LinearLayoutManager(this@DetailPlanActivity)
                            setHasFixedSize(true)
                            adapter = downtimePlanAdapter
                            downtimePlanAdapter.notifyDataSetChanged()
                        }
                    }
                }
                override fun onFailure(call: Call<DowntimeResponse>, t: Throwable) {
                    Log.e("Error", t.message!!)
                }
            })
        }
    }

    override fun onStop() {
        super.onStop()
        echo?.disconnect()
    }
    override fun onDestroy() {
        super.onDestroy()
        echo?.disconnect()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        echo?.disconnect()
        startActivity(Intent(this, DetailStationActivity::class.java))
    }
}