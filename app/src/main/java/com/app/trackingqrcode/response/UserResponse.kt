package com.app.trackingqrcode.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserResponse {
    @SerializedName("data")
    @Expose
    var data : User? = null

    class User {
        @SerializedName("name")
        @Expose
        var name : String? = null
    }
}
