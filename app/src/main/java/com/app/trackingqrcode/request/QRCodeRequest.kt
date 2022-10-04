package com.app.trackingqrcode.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class QRCodeRequest {
    @SerializedName("qr")
    @Expose
    var qr : String? = null
}