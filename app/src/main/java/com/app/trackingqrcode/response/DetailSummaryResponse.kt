package com.app.trackingqrcode.response

import com.app.trackingqrcode.model.DetailSummaryData
import com.google.gson.annotations.SerializedName

data class DetailSummaryResponse(

	@SerializedName("data")
	val data: DetailSummaryData? = null,

	@SerializedName("success")
	val success: Boolean? = null
)
