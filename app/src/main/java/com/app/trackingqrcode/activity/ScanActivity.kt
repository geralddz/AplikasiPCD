package com.app.trackingqrcode.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.trackingqrcode.R
import com.app.trackingqrcode.adapter.RejectionAdapter
import com.app.trackingqrcode.utils.ApiUtils
import com.app.trackingqrcode.request.QRCodeRequest
import com.app.trackingqrcode.response.QRResponse
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import kotlinx.android.synthetic.main.activity_scan.*
import kotlinx.android.synthetic.main.activity_scan.Vefficiency
import kotlinx.android.synthetic.main.activity_scan.Voee
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class ScanActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var name: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        codeScanner = CodeScanner(this, scanner_view)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        hiddenhasil()

        back.setOnClickListener{
            startActivity(Intent(this,HomeActivity::class.java))
        }

        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 1)
        }


        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                name = it.text
                tvHasil.text = name
                tvHasil.visibility = View.GONE
                scanned()
            }
        }

        scanner_view.setOnClickListener {
            codeScanner.startPreview()

        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun scanned() {
        val request = QRCodeRequest()
        request.qr = tvHasil.text.toString().trim()
        val retro = ApiUtils().getUserService()
        retro.scan(request).enqueue(object : Callback<QRResponse> {
            @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
            override fun onResponse(call: Call<QRResponse>, response: Response<QRResponse>) {
                val qr = response.body()
                val success = qr?.success
                val reject = qr?.listrejection
                val rejectionadapter = RejectionAdapter(reject)
                if (success==true) {
                    Toast.makeText(applicationContext, "Load Berhasil", Toast.LENGTH_LONG).show()
                    val target = qr.recap?.Target
                    val actual = qr.recap?.Actual
                    val rejectqr = qr.recap?.Rejection
                    val avail = qr.recap?.Avaibility?.toInt()
                    val perform = qr.recap?.Performance?.toInt()


                    tv1.text = qr.data?.operatorName.toString()
                    tv2.text = qr.data?.operatorNpk.toString()
                    tv3.text = qr.data?.partName.toString()
                    tv4.text = qr.data?.pnCust.toString()
                    tv5.text = qr.data?.customer.toString()
                    tv6.text = qr.data?.stationName.toString()
                    tv7.text = qr.data?.timestamp.toString()

                    if (qr.data?.status.toString() == "OK"){
                        tvOK.visibility = View.VISIBLE
                        tvOK.text = qr.data?.status.toString()
                        tvNG.visibility = View.GONE
                    }else {
                        tvNG.visibility = View.VISIBLE
                        tvNG.text = qr.data?.status.toString()
                        tvOK.visibility = View.GONE
                    }


//                    if(efficiency != null && efficiency != 0){
//                        Vefficiency.text = "$efficiency%"
//                    }else{
//                        Vefficiency.text = "0%"
//                    }
//
//                    if(oee != null && oee != 0){
//                        Voee.text = "$oee%"
//                    }else{
//                        Voee.text = "0%"
//                    }

                    if(avail != null && avail != 0){
                        pavail.text = "$avail%"
                    }else{
                        pavail.text = "0%"
                    }

                    if(perform != null && perform != 0){
                        pperform.text = "$perform%"
                    }else{
                        pperform.text = "0%"
                    }

                    if((target!=null && actual!=null && rejectqr!=null)){
                        val actuall = actual.toFloat()
                        val targett = target.toFloat()
                        val rejecttt = rejectqr.toFloat()
                        val hasilok = (actuall.plus(rejecttt))
                        val okeratio = ((actuall.div(hasilok)).times(100))
                        val hasilreject = ((rejecttt.div(hasilok)).times(100))
                        val achievement = (actuall.div(targett)).times(100)
                        val targetpersen = 100.div(targett).times(targett).toInt()
                        val actualpersen = 100.div(targett).times(actuall).toInt()

                        if(!okeratio.isNaN() && !achievement.isNaN() && !hasilreject.isNaN()){
                            val number3digits = (hasilreject * 1000.0).roundToInt() / 1000.0
                            val number2digits = (number3digits * 100.0).roundToInt() / 100.0
                            val rejectdec = (number2digits * 10.0).roundToInt() / 10.0

                            if (avail!=null&&perform!=null) {
                                val oee = (avail.times(perform).times(okeratio)).div(10000).toInt()
                                Voee.text = "$oee%"
                            }

                            Vefficiency.text = "${achievement.toInt()}%"
                            Pach.text = "${achievement.toInt()}%"
                            Prejec.text = "$rejectdec%"
                            pokr.text = "${okeratio.roundToInt()}%"
                            ptrgt.text = target.toString()
                            pact.text = actual.toString()

                            pbTarget.progressTintList = ColorStateList.valueOf(Color.GREEN)
                            pbTarget.progress = targetpersen

                            if (actualpersen < 70) {
                                pbActual.progressTintList = ColorStateList.valueOf(Color.RED)
                                pbActual.progress = actualpersen
                            }else if(actualpersen in 70..80){
                                pbActual.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                                pbActual.progress = actualpersen
                            }else{
                                pbActual.progressTintList = ColorStateList.valueOf(Color.GREEN)
                                pbActual.progress = actualpersen
                            }

                            if (okeratio.roundToInt() < 70) {
                                pbOk.progressTintList = ColorStateList.valueOf(Color.RED)
                                pbOk.progress = okeratio.roundToInt()
                            } else if (okeratio.roundToInt() in 70..80) {
                                pbOk.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                                pbOk.progress = okeratio.roundToInt()
                            } else {
                                pbOk.progressTintList = ColorStateList.valueOf(Color.GREEN)
                                pbOk.progress = okeratio.roundToInt()
                            }
                        }else{
                            Vefficiency.text = "0%"
                            Pach.text = "0%"
                            Prejec.text = "0%"
                            pokr.text = "0%"
                            ptrgt.text = "0"
                            pact.text = "0"
                        }
                    }else{
                        Vefficiency.text = "0%"
                        Pach.text = "0%"
                        Prejec.text = "0%"
                        pokr.text = "0%"
                        ptrgt.text = "0"
                        pact.text = "0"
                    }

                    if (avail != null) {
                        if (avail < 70){
                            pbavail.progressTintList = ColorStateList.valueOf(Color.RED)
                            pbavail.progress = avail
                        }else if(avail in 70..80){
                            pbavail.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                            pbavail.progress = avail
                        }else{
                            pbavail.progressTintList = ColorStateList.valueOf(Color.GREEN)
                            pbavail.progress = avail
                        }
                    }else{
                        pbavail.progressTintList = ColorStateList.valueOf(Color.RED)
                        pbavail.progress = 0
                    }

                    if (perform != null) {
                        if (perform < 70) {
                            pbperformance.progressTintList = ColorStateList.valueOf(Color.RED)
                            pbperformance.progress = perform
                        }else if(perform in 70..80){
                            pbperformance.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                            pbperformance.progress = perform
                        }else{
                            pbperformance.progressTintList = ColorStateList.valueOf(Color.GREEN)
                            pbperformance.progress = perform
                        }
                    }else{
                        pbperformance.progressTintList = ColorStateList.valueOf(Color.RED)
                        pbperformance.progress = 0
                    }

                    rvrejection.apply {
                        layoutManager = LinearLayoutManager(this@ScanActivity)
                        setHasFixedSize(true)
                        adapter = rejectionadapter
                        rejectionadapter.notifyDataSetChanged()
                    }
                    showhasil()
                }else{
                    hiddenhasil()
                    Toast.makeText(applicationContext, "QR Code Tidak Terdaftar", Toast.LENGTH_LONG
                    ).show()
                }

            }

            override fun onFailure(call: Call<QRResponse>, t: Throwable) {
                Log.e("Error", t.message!!)
            }

        })
    }

    private fun hiddenhasil(){
        tv1.visibility = View.GONE
        tv2.visibility = View.GONE
        tv3.visibility = View.GONE
        tv4.visibility = View.GONE
        tv5.visibility = View.GONE
        tv6.visibility = View.GONE
        tv7.visibility = View.GONE
        rvrejection.visibility = View.GONE
        scroll.visibility = View.GONE
        rv.visibility = View.GONE

    }

    private fun showhasil(){
        tv1.visibility = View.VISIBLE
        tv2.visibility = View.VISIBLE
        tv3.visibility = View.VISIBLE
        tv4.visibility = View.VISIBLE
        tv5.visibility = View.VISIBLE
        tv6.visibility = View.VISIBLE
        tv7.visibility = View.VISIBLE
        rvrejection.visibility = View.VISIBLE
        scroll.visibility = View.VISIBLE
        rv.visibility = View.VISIBLE

    }
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
    }
}