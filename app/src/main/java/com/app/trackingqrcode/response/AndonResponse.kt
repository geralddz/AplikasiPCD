package com.app.trackingqrcode.response

import com.google.gson.annotations.SerializedName

data class AndonResponse(

	@field:SerializedName("data")
	val data: List<DataAndon>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class DataAndon(

	@field:SerializedName("downtime_id")
	val downtimeId: Int? = null,

	@field:SerializedName("description")
	val description: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("planning_id")
	val planningId: Int? = null,

	@field:SerializedName("finish_time")
	val finishTime: String? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("start_time")
	val startTime: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("escalation")
	val escalation: Int? = null,

	@field:SerializedName("station_num")
	val stationNum: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("total_time")
	val totalTime: Int? = null,

	@field:SerializedName("finish_by")
	val finishBy: Any? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
