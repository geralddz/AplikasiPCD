package com.app.trackingqrcode.response

import com.google.gson.annotations.SerializedName

data class DetailPlanResponse(

	@field:SerializedName("actual")
	val actual: Int? = null,

	@field:SerializedName("efficiency")
	val efficiency: Int? = null,

	@field:SerializedName("cum_target")
	val cumTarget: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("planning_id")
	val planningId: Int? = null,

	@field:SerializedName("rejection")
	val rejection: Int? = null,

	@field:SerializedName("oee")
	val oee: Int? = null,

	@field:SerializedName("downtime")
	val downtime: Int? = null,

	@field:SerializedName("dandory")
	val dandory: Int? = null,

	@field:SerializedName("avaibility")
	val avaibility: Int? = null,

	@field:SerializedName("performance")
	val performance: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("time_start_prod")
	val timeStartProd: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("total_time")
	val totalTime: Int? = null
)
