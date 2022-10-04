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
    private var selectedStation = ""
    private var selectedCustomer = ""
    private var selectedType= ""
    private var selectedPart = ""
    private val summarylist: MutableList<SummaryList> = ArrayList()

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
            rvHistory.visibility = View.GONE
            swiper.isRefreshing = false
            rvHistory.visibility = View.VISIBLE
            stopShimmer()
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
                Filter(selectedStation, selectedCustomer, selectedType, selectedPart)

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        filterStation.setOnItemClickListener { parent, view, position, id ->
            selectedStation = parent.getItemAtPosition(position).toString()
            Filter(selectedStation, selectedCustomer, selectedType, selectedPart)
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
                Filter(selectedStation, selectedCustomer, selectedType, selectedPart)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        filterCostumer.setOnItemClickListener { parent, view, position, id ->
            selectedCustomer = parent.getItemAtPosition(position).toString()
            Filter(selectedStation, selectedCustomer, selectedType, selectedPart)
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
                Filter(selectedStation, selectedCustomer, selectedType, selectedPart)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        filterType.setOnItemClickListener { parent, view, position, id ->
            selectedType = parent.getItemAtPosition(position).toString()
            Filter(selectedStation, selectedCustomer, selectedType, selectedPart)
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
        filterPart.setOnItemClickListener { parent, view, position, id ->
            selectedPart = parent.getItemAtPosition(position).toString()
            Filter(selectedStation, selectedCustomer, selectedType, selectedPart)
        }
    }

    private fun setAdapter(list: List<SummaryList>){
        val AdapterSummary = SummaryAdapter(this, list)
        rvHistory!!.adapter = AdapterSummary
        AdapterSummary.notifyDataSetChanged()
    }

    private fun Filter(selectedStation: String, selectedCustomer: String, selectedType: String, selectedPart: String) {
        if (selectedCustomer.isEmpty()&&selectedPart.isEmpty()&&selectedType.isEmpty()){
            val station  = QueryStation(selectedStation)
            setAdapter(station)
        } else if (selectedStation.isEmpty()&&selectedPart.isEmpty()&&selectedType.isEmpty()) {
            val customer = QueryCustomer(selectedCustomer)
            setAdapter(customer)
        } else if (selectedStation.isEmpty()&&selectedPart.isEmpty()&&selectedCustomer.isEmpty()){
            val type = QueryType(selectedType)
            setAdapter(type)
        } else if (selectedStation.isEmpty()&&selectedType.isEmpty()&&selectedCustomer.isEmpty()){
            val part = QueryProduct(selectedPart)
            setAdapter(part)
        }else if (selectedPart.isEmpty()&&selectedType.isEmpty()){
            val stationcust = QueryStationCustomer(selectedStation,selectedCustomer)
            setAdapter(stationcust)
        }else if (selectedStation.isEmpty()&&selectedCustomer.isEmpty()){
            val typepart = QueryTypePart(selectedPart,selectedType)
            setAdapter(typepart)
        }else if (selectedStation.isEmpty()&&selectedType.isEmpty()){
            val custpart = QueryCustomerPart(selectedCustomer, selectedPart)
            setAdapter(custpart)
        }else if (selectedCustomer.isEmpty()&&selectedPart.isEmpty()){
            val stationtype = QueryStationType(selectedStation, selectedType)
            setAdapter(stationtype)
        }else if (selectedCustomer.isEmpty()&&selectedType.isEmpty()){
            val stationpart = QueryStationPart(selectedStation, selectedPart)
            setAdapter(stationpart)
        }else if (selectedStation.isEmpty()&&selectedPart.isEmpty()){
            val custype = QueryCustomerType(selectedCustomer, selectedType)
            setAdapter(custype)
        }else if (selectedStation.isEmpty()){
            val custypepart = QueryCustomerTypePart(selectedCustomer, selectedType, selectedPart)
            setAdapter(custypepart)
        }else if (selectedCustomer.isEmpty()){
            val stationtypepart = QueryStationTypePart(selectedStation, selectedType, selectedPart)
            setAdapter(stationtypepart)
        }else if (selectedType.isEmpty()){
            val statcustpart = QueryStationCustomerPart(selectedStation, selectedCustomer, selectedPart)
            setAdapter(statcustpart)
        }else if (selectedPart.isEmpty()){
            val statcustype = QueryStationCustomerType(selectedStation, selectedCustomer, selectedType)
            setAdapter(statcustype)
        }else{
            val listall = QueryAll(selectedStation,selectedCustomer,selectedType,selectedPart)
            setAdapter(listall)
        }
    }

    private fun QueryAll(selectedStation: String, selectedCustomer: String, selectedType: String, selectedPart: String): List<SummaryList> {
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

    private fun QueryStationCustomerType(selectedStation: String, selectedCustomer: String, selectedType: String): List<SummaryList> {
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

    private fun QueryStationCustomerPart(selectedStation: String, selectedCustomer: String, selectedPart: String): List<SummaryList> {
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

    private fun QueryCustomerTypePart(selectedCustomer: String, selectedType: String, selectedPart: String): List<SummaryList> {
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

    private fun QueryStationTypePart(selectedStation: String, selectedType: String, selectedPart: String): List<SummaryList> {
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

    private fun QueryStationCustomer(selectedStation: String, selectedCustomer: String): List<SummaryList> {
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

    private fun QueryStationType(selectedStation: String, selectedType: String): List<SummaryList> {
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

    private fun QueryStationPart(selectedStation: String, selectedPart: String): List<SummaryList> {
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

    private fun QueryCustomerType(selectedCustomer: String, selectedType: String): List<SummaryList> {
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

    private fun QueryCustomerPart(selectedCustomer: String, selectedPart: String): List<SummaryList> {
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

    private fun QueryTypePart(selectedType: String, selectedPart: String): List<SummaryList> {
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


    private fun QueryStation(selectedStation: String): List<SummaryList> {
        val filteredList: MutableList<SummaryList> = ArrayList()
        for (item in summarylist ) {
            if (item.stationNum.lowercase(Locale.ROOT).contains(selectedStation.lowercase())){
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun QueryCustomer(selectedCustomer: String): List<SummaryList> {
        val filteredList: MutableList<SummaryList> = ArrayList()
        for (item in summarylist) {
            if (item.customer.lowercase(Locale.ROOT).contains(selectedCustomer.lowercase())) {
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun QueryType(selectedType: String): List<SummaryList> {
        val filteredList: MutableList<SummaryList> = ArrayList()
        for (item in summarylist ) {
            if (item.type.lowercase(Locale.ROOT).contains(selectedType.lowercase())){
                filteredList.add(item)
            }
        }
        return filteredList
    }

    private fun QueryProduct(selectedPart: String): List<SummaryList> {
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
            override fun onResponse(call: Call<SummaryResponse>, response: Response<SummaryResponse>)
            {
                val summary = response.body()!!.data
                for (i in summary.indices){
                    val partid = summary[i].id
                    val partcus = summary[i].customer
                    val parttype = summary[i].type
                    val partlist = summary[i].partName
                    val stationlist = summary[i].stationNum
                    val single_stationlist = stationlist?.split(", ")?.toTypedArray()

                    for(single in single_stationlist!!){
                        val StationResponse = retro.getStationId(single)
                        StationResponse!!.enqueue(object : Callback<StationIdResponse?> {
                            @SuppressLint("NotifyDataSetChanged")
                            override fun onResponse(call: Call<StationIdResponse?>, response: Response<StationIdResponse?>) {
                                val station = response.body()!!.data
                                val stationid = station?.id
                                Log.e("Station ID", "onResponse: $stationid")

                                summarylist.addAll(
                                    listOf(SummaryList(single, partlist!!, stationid!!, partid!!, partcus!!, parttype!!))
                                )


                                val summaryadapter = SummaryAdapter(
                                    this@SummaryActivity,
                                    summarylist as List<SummaryList>
                                )
                                rvHistory.apply {
                                    layoutManager = LinearLayoutManager(this@SummaryActivity)
                                    setHasFixedSize(true)
                                    adapter = summaryadapter
                                    summaryadapter.notifyDataSetChanged()
                                }
                                stopShimmer()

                            }
                            override fun onFailure(call: Call<StationIdResponse?>, t: Throwable) {
                                Log.e("Error", t.message!!)
                            }
                        })
                        if (!Lstation_num.contains(single)) {
                            Lstation_num.add(single)
                        }
                        if (!Lcustomer.contains(partcus)) {
                            Lcustomer.add(partcus!!)
                        }
                        if (!Ltype.contains(parttype)) {
                            Ltype.add(parttype!!)
                        }
                        if (!Lpart.contains(partlist)) {
                            Lpart.add(partlist!!)
                        }
                    }
                }

            }
            override fun onFailure(call: Call<SummaryResponse>, t: Throwable) {
                Log.e("Error", t.message!!)
            }
        })

    }


    fun setShimmer() {
        shimmer.showShimmer(true)
        shimmer.setVisibility(View.VISIBLE)
        rvHistory.setVisibility(View.GONE)

    }

    fun hidefilter() {
        filterCostumer.visibility = View.GONE
        dropdown2.visibility = View.GONE
        filterType.visibility = View.GONE
        dropdown3.visibility = View.GONE
        filterPart.visibility = View.GONE
        dropdown4.visibility = View.GONE
    }

    fun stopShimmer() {
        shimmer.stopShimmer()
        shimmer.setVisibility(View.GONE)
        rvHistory.setVisibility(View.VISIBLE)

    }
}