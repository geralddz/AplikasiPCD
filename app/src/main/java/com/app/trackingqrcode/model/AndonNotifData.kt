package com.app.trackingqrcode.model

import com.google.gson.annotations.SerializedName

data class AndonNotifData(

	@field:SerializedName("station_name")
	val stationName: String? = null,

	@field:SerializedName("department_id")
	val departmentId: Any? = null,

	@field:SerializedName("dept_name")
	val deptName: String? = null,

	@field:SerializedName("result_downtime_id")
	val resultDowntimeId: Any? = null,

	@field:SerializedName("downtime_category")
	val downtimeCategory: String? = null
)
