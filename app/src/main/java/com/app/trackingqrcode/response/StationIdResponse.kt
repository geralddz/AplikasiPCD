package com.app.trackingqrcode.response

import com.app.trackingqrcode.model.StationData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StationIdResponse(

	@SerializedName("data")
	@Expose
	val data: StationData? = null,

	@SerializedName("success")
	@Expose
	val success: Boolean? = null,

	@SerializedName("message")
	@Expose
	val message: String? = null
)

