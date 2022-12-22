package com.app.trackingqrcode.response

import com.google.gson.annotations.SerializedName

data class LiveStationResponse(

	@field:SerializedName("data")
	val data: List<DataLive?>? = null
)

data class DataLive(

	@field:SerializedName("station_id")
	val stationId: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("is_downtime")
	val isDowntime: Int? = null,

	@field:SerializedName("planning_id")
	val planningId: Int? = null,

	@field:SerializedName("part_name")
	val partName: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
