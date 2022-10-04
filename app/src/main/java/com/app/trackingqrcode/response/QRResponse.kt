package com.app.trackingqrcode.response

import com.app.trackingqrcode.model.QRData
import com.app.trackingqrcode.model.RecapItem
import com.app.trackingqrcode.model.RejectionItem
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class QRResponse(

	@SerializedName("data")
	@Expose
	val data: QRData? = null,

	@SerializedName("success")
	@Expose
	val success: Boolean? = null,

	@SerializedName("rejection")
	@Expose
	val listrejection: List<RejectionItem?>? = null,

	@SerializedName("recap")
	@Expose
	val recap: RecapItem? = null

)

