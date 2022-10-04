package com.app.trackingqrcode.response

import com.app.trackingqrcode.model.SummaryData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SummaryResponse(

	@SerializedName("data")
	@Expose
	val data: List<SummaryData>
)
