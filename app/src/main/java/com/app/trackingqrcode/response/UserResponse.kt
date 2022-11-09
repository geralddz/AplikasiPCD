package com.app.trackingqrcode.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("access_token")
	val accessToken: Any? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("token_type")
	val tokenType: String? = null
)

data class Data(

	@field:SerializedName("mapping")
	val mapping: String? = null,

	@field:SerializedName("department_id")
	val departmentId: Int? = null,

	@field:SerializedName("last_login")
	val lastLogin: String? = null,

	@field:SerializedName("npk")
	val npk: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("op_skill")
	val opSkill: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("rfid_tag")
	val rfidTag: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("usergroup")
	val usergroup: String? = null,

	@field:SerializedName("updated_by")
	val updatedBy: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
