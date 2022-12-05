package com.app.trackingqrcode.response

import com.app.trackingqrcode.model.DetailStationData
import com.google.gson.annotations.SerializedName

data class DetailStationResponse(

	@field:SerializedName("data")
	val data: List<DetailStation>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null
)

data class DetailStation(

	@field:SerializedName("start_time")
	var startTime: String? = null,

	@field:SerializedName("target")
	var target: Int? = null,

	@field:SerializedName("status_flag")
	var statusFlag: String? = null,

	@field:SerializedName("break_s1")
	var breakS1: String? = null,

	@field:SerializedName("cycle_time")
	var cycleTime: Int? = null,

	@field:SerializedName("cavity")
	var cavity: String? = null,

	@field:SerializedName("total_time")
	var totalTime: Int? = null,

	@field:SerializedName("sku")
	var sku: String? = null,

	@field:SerializedName("break_s3")
	var breakS3: String? = null,

	@field:SerializedName("finish_time")
	var finishTime: String? = null,

	@field:SerializedName("plan_id")
	var planId: Int? = null,

	@field:SerializedName("part_name")
	var partName: String? = null
)
