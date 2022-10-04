package com.app.trackingqrcode.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DetailSummaryData(

    @SerializedName("actual")
    @Expose
    val actual: Int? = null,

    @SerializedName("efficiency")
    @Expose
    val efficiency: Int? = null,

    @SerializedName("cum_target")
    @Expose
    val cumTarget: Int? = null,

    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,

    @SerializedName("planning_id")
    @Expose
    val planningId: Int? = null,

    @SerializedName("finish_time")
    @Expose
    val finishTime: String? = null,

    @SerializedName("rejection")
    @Expose
    val rejection: Int? = null,

    @SerializedName("oee")
    @Expose
    val oee: Int? = null,

    @SerializedName("target")
    @Expose
    val target: Int? = null,

    @SerializedName("start_time")
    @Expose
    val startTime: String? = null,

    @SerializedName("operator_name")
    @Expose
    val operatorName: String? = null,

    @SerializedName("downtime")
    @Expose
    val downtime: Int? = null,

    @SerializedName("dandory")
    @Expose
    val dandory: Int? = null,

    @SerializedName("avaibility")
    @Expose
    val avaibility: Int? = null,

    @SerializedName("performance")
    @Expose
    val performance: Int? = null,

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null,

    @SerializedName("user_id")
    @Expose
    val userId: Int? = null,

    @SerializedName("cycle_time")
    @Expose
    val cycleTime: Int? = null,

    @SerializedName("time_start_prod")
    @Expose
    val timeStartProd: String? = null,

    @SerializedName("id")
    @Expose
    val id: Int? = null,

    @SerializedName("sku")
    @Expose
    val sku: String? = null,

    @SerializedName("total_time")
    @Expose
    val totalTime: Int? = null,

    @SerializedName("part_name")
    @Expose
    val partName: String? = null,

    @SerializedName("status")
    @Expose
    val status: Int? = null
)