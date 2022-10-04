package com.app.trackingqrcode.activity

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.trackingqrcode.R
import com.app.trackingqrcode.adapter.AndonAdapter
import com.app.trackingqrcode.model.AndonData
import kotlinx.android.synthetic.main.activity_andon.*
import kotlinx.android.synthetic.main.activity_andon.back
import kotlinx.android.synthetic.main.activity_live_monitoring.*
import kotlinx.android.synthetic.main.item_andon.*

class AndonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_andon)

        back.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        rvandon.layoutManager = LinearLayoutManager(this)

        modelAdapter()
    }

    private fun modelAdapter() {
        val andon = listOf(
            AndonData("Station-1060-1 Mengalami Downtime ", "Penyebab : Kuras Manifold", "10:00"),
            AndonData("Station-1060-2 Mengalami Downtime ", "Penyebab : Printer Rusak", "12:00"),
            AndonData("Station-1060-3 Mengalami Downtime ", "Penyebab : Komputer Ngelag", "09:00"),
            AndonData("Station-1060-4 Mengalami Downtime ", "Penyebab : Operator Sakit", "14:00"),
            AndonData("Station-1060-5 Mengalami Downtime ", "Penyebab : LCD Rusak", "18:00"),
            AndonData("Station-1060-6 Mengalami Downtime ", "Penyebab : Keyboard Hilang", "15:00")
        )
        val andonAdapter = AndonAdapter(andon)
        rvandon.adapter=andonAdapter
    }
}