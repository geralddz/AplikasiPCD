package com.app.trackingqrcode.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StationResponse(

	@SerializedName("data")
	val data: List<DataStation>? = null,

	@SerializedName("success")
	val success: Boolean? = null,

	@SerializedName("message")
	val message: String? = null
)

data class DataStation(

	@SerializedName("updated_at")
	val updatedAt: String? = null,

	@SerializedName("downtime_tolerance")
	val downtimeTolerance: Int? = null,

	@SerializedName("line")
	val line: String? = null,

	@SerializedName("warning_dandory_id")
	val warningDandoryId: Int? = null,

	@SerializedName("name")
	val name: String? = null,

	@SerializedName("created_at")
	val createdAt: String? = null,

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("dandory_warning")
	val dandoryWarning: String? = null
)
