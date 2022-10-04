package com.app.trackingqrcode.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.app.trackingqrcode.R
import com.app.trackingqrcode.api.Session
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    private lateinit var session: Session
    private lateinit var name : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        session = Session(this)
        name = session.getName().toString()
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
                .setPositiveButton("Yes") { dialog: DialogInterface?, which: Int ->
                    session.isSignOut()
                    Toast.makeText(this, "Sign Out Berhasil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                }.setNegativeButton("No") { dialog: DialogInterface, which: Int ->
                    dialog.cancel()
                }.show()

        }
    }
}