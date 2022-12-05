package com.app.trackingqrcode.model

import com.google.gson.annotations.SerializedName

data class DetailStationData(
    var startTime: String?,
    var statusFlag: String?,
    var cycleTime: Int?,
    var target: Int?,
    var sku: String?,
    var finishTime: String?,
    var planId: Int?,
    var partName: String?,
    var shift : String?
)