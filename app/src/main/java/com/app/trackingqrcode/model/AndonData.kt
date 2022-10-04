package com.app.trackingqrcode.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AndonData(

    @SerializedName("Head")
    @Expose
    val head: String? = null,

    @SerializedName("Reason")
    @Expose
    val reason: String? = null,

    @SerializedName("Time")
    @Expose
    val time: String? = null

)