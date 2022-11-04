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
import com.app.trackingqrcode.api.ApiUtils
import com.app.trackingqrcode.request.QRCodeRequest
import com.app.trackingqrcode.response.QRResponse
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import kotlinx.android.synthetic.main.activity_scan.*
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

                    Vefficiency.text = qr.recap?.Efficiency.toString()+"%"
                    Voee.text = qr.recap?.OEE.toString()+"%"
                    val actuall = qr.recap?.Actual.toString().toFloat()
                    val targett = qr.recap?.Target.toString().toFloat()
                    val rejecttt = qr.recap?.Rejection.toString().toInt()
                    val hasilok = (actuall+rejecttt)
                    val hasilcoba = ((actuall / hasilok) *100).roundToInt()
                    val hasilreject = ((rejecttt / hasilok) *100).roundToInt()
                    val targetpersen = 100.div(targett).times(targett)
                    val actualpersen = 100.div(targett).times(actuall)

                    Percentagereject.text = "$hasilreject%"
                    percentage.text = qr.recap?.Avaibility.toString()+"%"
                    percentage1.text = qr.recap?.Performance.toString()+"%"
                    percentagetrgt.text = qr.recap?.Target.toString()
                    percentageact.text = qr.recap?.Actual.toString()
                    percentage7.text = "$hasilcoba%"

                    val avail = qr.recap?.Avaibility.toString().toInt()
                    val perform = qr.recap?.Performance.toString().toInt()

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

                    pbTarget.progressTintList = ColorStateList.valueOf(Color.GREEN)
                    pbTarget.progress = targetpersen.toInt()

                    if (actualpersen < 70) {
                        pbActual.progressTintList = ColorStateList.valueOf(Color.RED)
                        pbActual.progress = actualpersen.toInt()
                    }else if(actualpersen.toInt() in 70..80){
                        pbActual.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                        pbActual.progress = actualpersen.toInt()
                    }else{
                        pbActual.progressTintList = ColorStateList.valueOf(Color.GREEN)
                        pbActual.progress = actualpersen.toInt()
                    }

                    if (hasilcoba < 70) {
                        pbOk.progressTintList = ColorStateList.valueOf(Color.RED)
                        pbOk.progress = hasilcoba
                    } else if (hasilcoba in 70..80) {
                        pbOk.progressTintList = ColorStateList.valueOf(Color.YELLOW)
                        pbOk.progress = hasilcoba
                    } else {
                        pbOk.progressTintList = ColorStateList.valueOf(Color.GREEN)
                        pbOk.progress = hasilcoba
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
                    Toast.makeText(
                        applicationContext, "QR Code Tidak Terdaftar", Toast.LENGTH_LONG
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
