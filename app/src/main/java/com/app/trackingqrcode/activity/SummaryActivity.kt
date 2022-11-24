package com.app.trackingqrcode.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.trackingqrcode.R
import com.app.trackingqrcode.adapter.SummaryAdapter
import com.app.trackingqrcode.api.ApiUtils
import com.app.trackingqrcode.model.SummaryList
import com.app.trackingqrcode.response.StationIdResponse
import com.app.trackingqrcode.response.SummaryResponse
import kotlinx.android.synthetic.main.activity_live_monitoring.*
import kotlinx.android.synthetic.main.activity_summary.*
import kotlinx.android.synthetic.main.activity_summary.back
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class SummaryActivity : AppCompatActivity() {
    private val Lstation_num = ArrayList<String>()
    private val Lcustomer = ArrayList<String>()
    private val Ltype = ArrayList<String>()
    private val Lpart = ArrayList<String>()
    private var adapter: SummaryAdapter? = null
    private var stationid:Int = 0
    private var selectedStation = ""
    private var selectedCustomer = ""
    private var selectedType = ""
    private var selectedPart = ""
    private var summarylist: MutableList<SummaryList> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        back.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        hidefilter()
        setShimmer()
        showSummary()

        swiper.setOnRefreshListener {
            setShimmer()
            summarylist = ArrayList()
            adapter?.notifyDataSetChanged()
            showSummary()
            val summaryadapter = SummaryAdapter(
                this@SummaryActivity, summarylist
            )
            rvSummary.apply {
                layoutManager = LinearLayoutManager(this@SummaryActivity)
                setHasFixedSize(true)
                adapter = summaryadapter
                summaryadapter.notifyDataSetChanged()
            }
            swiper.isRefreshing = false
        }

        val dataStation = ArrayAdapter(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, Lstation_num)
        val dataCustomer = ArrayAdapter(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, Lcustomer)
        val dataType = ArrayAdapter(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, Ltype)
        val dataPart = ArrayAdapter(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, Lpart)


        filterStation.setAdapter(dataStation)
        filterStation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                selectedStation = s.toString()
                filter(selectedStation, selectedCustomer, selectedType, selectedPart)

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        filterStation.setOnItemClickListener { parent, _, position, _ ->
            selectedStation = parent.getItemAtPosition(position).toString()
            filter(selectedStation, selectedCustomer, selectedType, selectedPart)
            filterCostumer.visibility = View.VISIBLE
            dropdown2.visibility = View.VISIBLE
            filterCostumer.requestFocus()
            dropdown2.requestFocus()

        }

        filterCostumer.setAdapter(dataCustomer)
        filterCostumer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                selectedCustomer = s.toString()
                filter(selectedStation, selectedCustomer, selectedType, selectedPart)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        filterCostumer.setOnItemClickListener { parent, _, position, _ ->
            selectedCustomer = parent.getItemAtPosition(position).toString()
            filter(selectedStation, selectedCustomer, selectedType, selectedPart)
            filterType.visibility = View.VISIBLE
            dropdown3.visibility = View.VISIBLE
            filterType.requestFocus()
            dropdown3.requestFocus()
        }

        filterType.setAdapter(dataType)
        filterType.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                selectedType = s.toString()
                filter(selectedStation, selectedCustomer, selectedType, selectedPart)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        filterType.setOnItemClickListener { parent, _, position, _ ->
            selectedType = parent.getItemAtPosition(position).toString()
            filter(selectedStation, selectedCustomer, selectedType, selectedPart)
            filterPart.visibility = View.VISIBLE
            dropdown4.visibility = View.VISIBLE
            filterPart.requestFocus()
            dropdown4.requestFocus()
        }

        filterPart.setAdapter(dataPart)
        filterPart.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                selectedPart = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        filterPart.setOnItemClickListener { parent, _, position, _ ->
            selectedPart = parent.getItemAtPosition(position).toString()
            filter(selectedStation, selectedCustomer, selectedType, selectedPart)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter(list: List<SummaryList>){
        val adapterSummary = SummaryAdapter(this, list)
        rvSummary!!.adapter = adapterSummary
        adapterSummary.notifyDataSetChanged()
    }

    private fun filter(selectedStation: String, selectedCustomer: String, selectedType: String, selectedPart: String) {
        if (selectedCustomer.isEmpty()&&selectedPart.isEmpty()&&selectedType.isEmpty()){
            val station  = queryStation(selectedStation)
            setAdapter(station)
        } else if (selectedStation.isEmpty()&&selectedPart.isEmpty()&&selectedType.isEmpty()) {
            val customer = queryCustomer(selectedCustomer)
            setAdapter(customer)
        } else if (selectedStation.isEmpty()&&selectedPart.isEmpty()&&selectedCustomer.isEmpty()){
            val type = queryType(selectedType)
            setAdapter(type)
        } else if (selectedStation.isEmpty()&&selectedType.isEmpty()&&selectedCustomer.isEmpty()){
            val part = queryProduct(selectedPart)
            setAdapter(part)
        }else if (selectedPart.isEmpty()&&selectedType.isEmpty()){
            val stationcust = queryStationCustomer(selectedStation,selectedCustomer)
            setAdapter(stationcust)
        }else if (selectedStation.isEmpty()&&selectedCustomer.isEmpty()){
            val typepart = queryTypePart(selectedPart,selectedType)
            setAdapter(typepart)
        }else if (selectedStation.isEmpty()&&selectedType.isEmpty()){
            val custpart = queryCustomerPart(selectedCustomer, selectedPart)
            setAdapter(custpart)
        }else if (selectedCustomer.isEmpty()&&selectedPart.isEmpty()){
            val stationtype = queryStationType(selectedStation, selectedType)
            setAdapter(stationtype)
        }else if (selectedCustomer.isEmpty()&&selectedType.isEmpty()){
            val stationpart = queryStationPart(selectedStation, selectedPart)
            setAdapter(stationpart)
        }else if (selectedStation.isEmpty()&&selectedPart.isEmpty()){
            val custype = queryCustomerType(selectedCustomer, selectedType)
            setAdapter(custype)
        }else if (selectedStation.isEmpty()){
            val custypepart = queryCustomerTypePart(selectedCustomer, selectedType, selectedPart)
            setAdapter(custypepart)
        }else if (selectedCustomer.isEmpty()){
            val stationtypepart = queryStationTypePart(selectedStation, selectedType, selectedPart)
            setAdapter(stationtypepart)
        }else if (selectedType.isEmpty()){
            val statcustpart = queryStationCustomerPart(selectedStation, selectedCustomer, selectedPart)
            setAdapter(statcustpart)
        }else if (selectedPart.isEmpty()){
            val statcustype = queryStationCustomerType(selectedStation, selectedCustomer, selectedType)
            setAdapter(statcustype)
        }else{
            val listall = queryAll(selectedStation,selectedCustomer,selectedType,selectedPart)
            setAdapter(listall)
        }
    }

    private fun queryAll(selectedStation: String, selectedCustomer: String, selectedType: String, selectedPart: String): List<SummaryList> {
        val filteredList: MutableList<SummaryList> = ArrayList()
        for (item in summarylist) {
            if (item.stationNum.lowercase(Locale.ROOT).contains(selectedStation.lowercase()) &&
                item.customer.lowercase(Locale.ROOT).contains(selectedCustomer.lowercase()) &&
                item.type.lowercase(Locale.ROOT).contains(selectedType.lowercase()) &&
                item.partName.lowercase(Locale.ROOT).contains(selectedPart.lowercase())
            ) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun queryStationCustomerType(selectedStation: String, selectedCustomer: String, selectedType: String): List<SummaryList> {
        val filteredList: MutableList<SummaryList> = ArrayList()
        for (item in summarylist) {
            if (item.stationNum.lowercase(Locale.ROOT).contains(selectedStation.lowercase()) &&
                item.customer.lowercase(Locale.ROOT).contains(selectedCustomer.lowercase()) &&
                item.type.lowercase(Locale.ROOT).contains(selectedType.lowercase())
            ) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun queryStationCustomerPart(selectedStation: String, selectedCustomer: String, selectedPart: String): List<SummaryList> {
        val filteredList: MutableList<SummaryList> = ArrayList()
        for (item in summarylist) {
            if (item.stationNum.lowercase(Locale.ROOT).contains(selectedStation.lowercase()) &&
                item.customer.lowercase(Locale.ROOT).contains(selectedCustomer.lowercase()) &&
                item.partName.lowercase(Locale.ROOT).contains(selectedPart.lowercase())
            ) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun queryCustomerTypePart(selectedCustomer: String, selectedType: String, selectedPart: String): List<SummaryList> {
        val filteredList: MutableList<SummaryList> = ArrayList()
        for (item in summarylist) {
            if (item.type.lowercase(Locale.ROOT).contains(selectedType.lowercase()) &&
                item.customer.lowercase(Locale.ROOT).contains(selectedCustomer.lowercase()) &&
                item.partName.lowercase(Locale.ROOT).contains(selectedPart.lowercase())
            ) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun queryStationTypePart(selectedStation: String, selectedType: String, selectedPart: String): List<SummaryList> {
        val filteredList: MutableList<SummaryList> = ArrayList()
        for (item in summarylist) {
            if (item.stationNum.lowercase(Locale.ROOT).contains(selectedStation.lowercase()) &&
                item.type.lowercase(Locale.ROOT).contains(selectedType.lowercase()) &&
                item.partName.lowercase(Locale.ROOT).contains(selectedPart.lowercase())
            ) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun queryStationCustomer(selectedStation: String, selectedCustomer: String): List<SummaryList> {
        val filteredList: MutableList<SummaryList> = ArrayList()
        for (item in summarylist) {
            if (item.stationNum.lowercase(Locale.ROOT).contains(selectedStation.lowercase()) &&
                item.customer.lowercase(Locale.ROOT).contains(selectedCustomer.lowercase())
            ) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun queryStationType(selectedStation: String, selectedType: String): List<SummaryList> {
        val filteredList: MutableList<SummaryList> = ArrayList()
        for (item in summarylist) {
            if (item.stationNum.lowercase(Locale.ROOT).contains(selectedStation.lowercase()) &&
                item.type.lowercase(Locale.ROOT).contains(selectedType.lowercase())
            ) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun queryStationPart(selectedStation: String, selectedPart: String): List<SummaryList> {
        val filteredList: MutableList<SummaryList> = ArrayList()
        for (item in summarylist) {
            if (item.stationNum.lowercase(Locale.ROOT).contains(selectedStation.lowercase()) &&
                item.partName.lowercase(Locale.ROOT).contains(selectedPart.lowercase())
            ) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun queryCustomerType(selectedCustomer: String, selectedType: String): List<SummaryList> {
        val filteredList: MutableList<SummaryList> = ArrayList()
        for (item in summarylist) {
            if (item.customer.lowercase(Locale.ROOT).contains(selectedCustomer.lowercase()) &&
                item.type.lowercase(Locale.ROOT).contains(selectedType.lowercase())
            ) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun queryCustomerPart(selectedCustomer: String, selectedPart: String): List<SummaryList> {
        val filteredList: MutableList<SummaryList> = ArrayList()
        for (item in summarylist) {
            if (item.customer.lowercase(Locale.ROOT).contains(selectedCustomer.lowercase()) &&
                item.partName.lowercase(Locale.ROOT).contains(selectedPart.lowercase())
            ) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun queryTypePart(selectedType: String, selectedPart: String): List<SummaryList> {
        val filteredList: MutableList<SummaryList> = ArrayList()
        for (item in summarylist) {
            if (item.type.lowercase(Locale.ROOT).contains(selectedType.lowercase()) &&
                item.partName.lowercase(Locale.ROOT).contains(selectedPart.lowercase())
            ) {
                filteredList.add(item)
            }
        }
        return filteredList
    }


    private fun queryStation(selectedStation: String): List<SummaryList> {
        val filteredList: MutableList<SummaryList> = ArrayList()
        for (item in summarylist ) {
            if (item.stationNum.lowercase(Locale.ROOT).contains(selectedStation.lowercase())){
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun queryCustomer(selectedCustomer: String): List<SummaryList> {
        val filteredList: MutableList<SummaryList> = ArrayList()
        for (item in summarylist) {
            if (item.customer.lowercase(Locale.ROOT).contains(selectedCustomer.lowercase())) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun queryType(selectedType: String): List<SummaryList> {
        val filteredList: MutableList<SummaryList> = ArrayList()
        for (item in summarylist ) {
            if (item.type.lowercase(Locale.ROOT).contains(selectedType.lowercase())){
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun queryProduct(selectedPart: String): List<SummaryList> {
        val filteredList: MutableList<SummaryList> = ArrayList()
        for (item in summarylist ) {
            if (item.partName.lowercase(Locale.ROOT).contains(selectedPart.lowercase())){
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun showSummary() {
        val retro = ApiUtils().getUserService()
        retro.getSummary().enqueue(object : Callback<SummaryResponse> {
            override fun onResponse(call: Call<SummaryResponse>, response: Response<SummaryResponse>) {
                val responsebody = response.body()
                val summary = responsebody?.data
                if (summary != null) {
                    for (i in summary.indices){
                        val partid = summary[i].id
                        val partcus = summary[i].customer
                        val parttype = summary[i].type
                        val partlist = summary[i].partName
                        val stationlist = summary[i].stationNum
                        val singleStationlist = stationlist?.split(", ")?.toTypedArray()
                        for(single in singleStationlist!!){
                            val stationResponse = retro.getStationId(single)
                            stationResponse!!.enqueue(object : Callback<StationIdResponse?> {
                                @SuppressLint("NotifyDataSetChanged")
                                override fun onResponse(call: Call<StationIdResponse?>, response: Response<StationIdResponse?>) {
                                    val stationrespon = response.body()
                                    val station = stationrespon?.data
                                    if (station != null){
                                        stationid = station.id!!
                                    }
                                    Log.e("Station ID", "onResponse: $stationid")

                                    summarylist.addAll(listOf(SummaryList(single, partlist!!,
                                        stationid, partid!!, partcus!!, parttype!!)))

                                    val summaryadapter = SummaryAdapter(
                                        this@SummaryActivity,
                                        summarylist as List<SummaryList>
                                    )
                                    rvSummary.apply {
                                        layoutManager = LinearLayoutManager(this@SummaryActivity)
                                        setHasFixedSize(true)
                                        adapter = summaryadapter
                                        summaryadapter.notifyDataSetChanged()
                                    }
                                    if (!Lstation_num.contains(single)) {
                                        Lstation_num.add(single)
                                    }
                                    if (!Lcustomer.contains(partcus)) {
                                        Lcustomer.add(partcus)
                                    }
                                    if (!Ltype.contains(parttype)) {
                                        Ltype.add(parttype)
                                    }
                                    if (!Lpart.contains(partlist)) {
                                        Lpart.add(partlist)
                                    }
                                    stopShimmer()
                                }

                                override fun onFailure(call: Call<StationIdResponse?>, t: Throwable) {
                                    Log.e("Error", t.message!!)
                                }
                            })
                        }
                    }
                }
            }
            override fun onFailure(call: Call<SummaryResponse>, t: Throwable) {
                Log.e("Error", t.message!!)
            }
        })

    }

    private fun setShimmer() {
        shimmer.showShimmer(true)
        shimmer.visibility = View.VISIBLE
        rvSummary.visibility = View.GONE

    }

    private fun hidefilter() {
        filterCostumer.visibility = View.GONE
        dropdown2.visibility = View.GONE
        filterType.visibility = View.GONE
        dropdown3.visibility = View.GONE
        filterPart.visibility = View.GONE
        dropdown4.visibility = View.GONE
    }

    private fun stopShimmer() {
        shimmer.stopShimmer()
        shimmer.visibility = View.GONE
        rvSummary.visibility = View.VISIBLE

    }
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, HomeActivity::class.java))
    }
}