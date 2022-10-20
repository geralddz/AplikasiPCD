package com.app.trackingqrcode.api

import com.app.trackingqrcode.request.QRCodeRequest
import com.app.trackingqrcode.request.UserRequest
import com.app.trackingqrcode.response.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @POST("login")
    fun login(@Body userRequest: UserRequest): Call<UserResponse>

    @POST("trace/qr")
    fun scan(@Body qrCodeRequest: QRCodeRequest): Call<QRResponse>

    @GET("product/select")
    fun getSummary() : Call<SummaryResponse>

    @GET("station")
    fun getStation(): Call<StationResponse>?

    @GET("station/station-num")
    fun getStationId(
        @Query("station_num") station_num: String?
    ): Call<StationIdResponse>?

    @POST("report/recap")
    fun getDetailSummary(
        @Query("station_id") station_id: String?,
        @Query("product_id") product_id: String?
    ): Call<DetailSummaryResponse>

    @GET("on-planning")
    fun getOnPlanning(
        @Query("station_id") station_id: Int?
    ): Call<OnPlanningResponse>

    @GET("planning/on-coming")
    fun getDetailStation(
        @Query("station_id") station_id: String,
    ): Call<DetailStationResponse>

    @GET("downtime/not-null")
    fun getDowntime(
        @Query("station_id") Idstation: Int?,
        @Query("planning_id") Idplanning: Int?
    ): Call<DowntimeResponse>

    @GET("dashboard/data")
    fun getDetailPlan(
        @Query("planning_id") Idplanning: String?
    ): Call<DetailPlanResponse>

}