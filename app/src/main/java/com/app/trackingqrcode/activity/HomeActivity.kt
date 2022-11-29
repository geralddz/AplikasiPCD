package com.app.trackingqrcode.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.app.trackingqrcode.R
import com.app.trackingqrcode.utils.SharedPref
import kotlinx.android.synthetic.main.activity_home.*
import net.mrbin99.laravelechoandroid.Echo

class HomeActivity : AppCompatActivity() {
    private lateinit var iduser: String
    private lateinit var departemendId: String
    private lateinit var sharedPref: SharedPref
    private lateinit var name: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sharedPref = SharedPref(this)
        name = sharedPref.getName().toString()
        iduser = sharedPref.getIdUser().toString()
        Log.e("iduser", "data: $iduser")
        departemendId = sharedPref.getDepartement().toString()
        Log.e("departemen_id", departemendId)
        tvUser.text = name

        live.setOnClickListener {
            startActivity(Intent(this, LiveMonitoringActivity::class.java))
        }
        history.setOnClickListener {
            startActivity(Intent(this, SummaryActivity::class.java))
        }

        scan.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }

        andon.setOnClickListener {
            startActivity(Intent(this, AndonActivity::class.java))
        }

        btnsignout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Caution !!")
                .setMessage("Apakah Anda Yakin Ingin Keluar ? ")
                .setIcon(R.drawable.logo)
                .setPositiveButton("Yes") { _: DialogInterface?, _: Int ->
                    sharedPref.isSignOut()
                    Toast.makeText(this, "Sign Out Berhasil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                }.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                    dialog.cancel()
                }.show()

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val exitIntent = Intent(Intent.ACTION_MAIN)
        exitIntent.addCategory(Intent.CATEGORY_HOME)
        exitIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(exitIntent)
    }
}