package com.app.trackingqrcode.utils

import com.app.trackingqrcode.request.QRCodeRequest
import com.app.trackingqrcode.response.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @POST("login")
    fun login(
        @Query("station_id") idStation: String?,
        @Query("username") username: String?,
        @Query("password") password: String?
    ): Call<UserResponse>

    @POST("trace/qr")
    fun scan(@Body qrCodeRequest: QRCodeRequest): Call<QRResponse>

    @GET("product/select")
    fun getSummary() : Call<SummaryResponse>

    @GET("live-station")
    fun getLiveStation(): Call<LiveStationResponse>?

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
        @Query("station_id") Idstation: String?,
        @Query("planning_id") Idplanning: String?
    ): Call<DowntimeResponse>

    @GET("dashboard/data")
    fun getDetailPlan(
        @Query("planning_id") Idplanning: String?
    ): Call<DetailPlanResponse>

    @GET("rejection/station-planning")
    fun getRejectionPlan(
        @Query("planning_id") Idplanning: String?,
        @Query("station_id") Idstation: String?
    ): Call<RejectionPlanResponse>

    @GET("downtime/department")
    fun getAndon(
        @Query("department_id") DepartId: String?,
    ): Call<AndonResponse>

}