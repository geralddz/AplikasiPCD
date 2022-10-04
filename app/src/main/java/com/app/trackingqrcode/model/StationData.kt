package com.app.trackingqrcode.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StationData(

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null,

    @SerializedName("downtime_tolerance")
    @Expose
    val downtimeTolerance: Int? = null,

    @SerializedName("line")
    @Expose
    val line: String? = null,

    @SerializedName("warning_dandory_id")
    @Expose
    val warningDandoryId: Int? = null,

    @SerializedName("name")
    @Expose
    val name: String? = null,

//    @SerializedName("status")
//    @Expose
//    val status: String? = null,

    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,

    @SerializedName("id")
    @Expose
    val id: Int? = null,

    @SerializedName("dandory_warning")
    @Expose
    val dandoryWarning: String? = null
)
