package com.app.trackingqrcode.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class QRData(

    @SerializedName("station_name")
    @Expose
    val stationName: String? = null,

    @SerializedName("operator_name")
    @Expose
    val operatorName: String? = null,

    @SerializedName("operator_npk")
    @Expose
    val operatorNpk: String? = null,

    @SerializedName("op_qc_status")
    @Expose
    val status: String? = null,

    @SerializedName("pn_cust")
    @Expose
    val pnCust: String? = null,

    @SerializedName("part_name")
    @Expose
    val partName: String? = null,

    @SerializedName("customer")
    @Expose
    val customer: String? = null,

    @SerializedName("timestamp")
    @Expose
    val timestamp: String? = null
)

data class RejectionItem(

    @SerializedName("reject")
    @Expose
    val reject: Int? = null,

    @SerializedName("name")
    @Expose
    val name: String? = null
)

data class RecapItem(
    @SerializedName("actual")
    @Expose
    val Actual: String? = null,

    @SerializedName("cum_target")
    @Expose
    val Target: String? = null,

    @SerializedName("avaibility")
    @Expose
    val Avaibility: String? = null,

    @SerializedName("performance")
    @Expose
    val Performance: String? = null,

    @SerializedName("efficiency")
    @Expose
    val Efficiency: String? = null,

    @SerializedName("downtime")
    @Expose
    val Downtime: String? = null,

    @SerializedName("dandory")
    @Expose
    val Dandory: String? = null,

    @SerializedName("rejection")
    @Expose
    val Rejection: String? = null,

    @SerializedName("oee")
    @Expose
    val OEE: String? = null,

    @SerializedName("total_time")
    @Expose
    val TotalTime: String? = null,

    )