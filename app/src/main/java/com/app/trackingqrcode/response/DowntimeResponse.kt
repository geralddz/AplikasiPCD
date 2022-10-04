package com.app.trackingqrcode.response

import com.google.gson.annotations.SerializedName

data class DowntimeResponse(

	@field:SerializedName("data")
	val data: List<DataDowntime?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class DataDowntime(

	@field:SerializedName("station_name")
	val stationName: String? = null,

	@field:SerializedName("downtime_id")
	val downtimeId: Int? = null,

	@field:SerializedName("description")
	val description: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("planning_id")
	val planningId: Int? = null,

	@field:SerializedName("downtime_category")
	val downtimeCategory: String? = null,

	@field:SerializedName("finish_time")
	val finishTime: Any? = null,

	@field:SerializedName("start_time")
	val startTime: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("escalation")
	val escalation: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("total_time")
	val totalTime: Any? = null,

	@field:SerializedName("finish_by")
	val finishBy: Any? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
