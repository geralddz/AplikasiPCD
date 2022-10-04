package com.app.trackingqrcode.response

import com.google.gson.annotations.SerializedName

data class OnPlanningResponse(

	@field:SerializedName("data")
	val data: List<DataOnPlanning?>? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataOnPlanning(

	@field:SerializedName("s1_br1s")
	val s1Br1s: Any? = null,

	@field:SerializedName("s1_br2s")
	val s1Br2s: Any? = null,

	@field:SerializedName("status_flag")
	val statusFlag: String? = null,

	@field:SerializedName("s1_br3s")
	val s1Br3s: Any? = null,

	@field:SerializedName("cavity")
	val cavity: Any? = null,

	@field:SerializedName("planning_id")
	val planningId: Int? = null,

	@field:SerializedName("s3_br2s")
	val s3Br2s: Any? = null,

	@field:SerializedName("s3_br1s")
	val s3Br1s: Any? = null,

	@field:SerializedName("finish_time")
	val finishTime: String? = null,

	@field:SerializedName("target")
	val target: Int? = null,

	@field:SerializedName("s1_br3e")
	val s1Br3e: Any? = null,

	@field:SerializedName("start_time")
	val startTime: String? = null,

	@field:SerializedName("s3_br3s")
	val s3Br3s: Any? = null,

	@field:SerializedName("downtime_tolerance")
	val downtimeTolerance: Int? = null,

	@field:SerializedName("qty_cavity")
	val qtyCavity: Any? = null,

	@field:SerializedName("s1_br1e")
	val s1Br1e: Any? = null,

	@field:SerializedName("s1_br2e")
	val s1Br2e: Any? = null,

	@field:SerializedName("s3_ots")
	val s3Ots: Any? = null,

	@field:SerializedName("s3_ote")
	val s3Ote: Any? = null,

	@field:SerializedName("total_time")
	val totalTime: Int? = null,

	@field:SerializedName("s3_br3e")
	val s3Br3e: Any? = null,

	@field:SerializedName("part_name")
	val partName: String? = null,

	@field:SerializedName("s3_br2e")
	val s3Br2e: Any? = null,

	@field:SerializedName("s3_br1e")
	val s3Br1e: Any? = null
)
