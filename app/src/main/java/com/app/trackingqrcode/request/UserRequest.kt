package com.app.trackingqrcode.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserRequest {
    @SerializedName("username")
    @Expose
    var username : String? = null

    @SerializedName("password")
    @Expose
    var password : String? = null

}