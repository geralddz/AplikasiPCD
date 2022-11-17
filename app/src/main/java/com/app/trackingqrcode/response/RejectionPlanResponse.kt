package com.app.trackingqrcode.response

import com.google.gson.annotations.SerializedName

data class RejectionPlanResponse(

	@field:SerializedName("data")
	val data: List<DataRejectionPlan>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class DataRejectionPlan(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("value")
	val value: Int? = null
)
