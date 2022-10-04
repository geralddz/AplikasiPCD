package com.app.trackingqrcode.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SummaryData(

    @SerializedName("pn_api")
    @Expose
    val pnApi: String? = null,

    @SerializedName("seq_qr")
    @Expose
    val seqQr: Int? = null,

    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,

    @SerializedName("type")
    @Expose
    val type: String? = null,

    @SerializedName("job_num")
    @Expose
    val jobNum: String? = null,

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null,

    @SerializedName("reject_tolerance")
    @Expose
    val rejectTolerance: Int? = null,

    @SerializedName("id")
    @Expose
    val id: Int? = null,

    @SerializedName("qty_assy")
    @Expose
    val qtyAssy: Int? = null,

    @SerializedName("sku")
    @Expose
    val sku: String? = null,

    @SerializedName("stock")
    @Expose
    val stock: Int? = null,

    @SerializedName("seq_wip_dtp")
    @Expose
    val seqWipDtp: Any? = null,

    @SerializedName("address")
    @Expose
    val address: String? = null,

    @SerializedName("inventory_id")
    @Expose
    val inventoryId: Int? = null,

    @SerializedName("cavity")
    @Expose
    val cavity: String? = null,

    @SerializedName("pn_cust")
    @Expose
    val pnCust: String? = null,

    @SerializedName("qty_packaging")
    @Expose
    val qtyPackaging: Int? = null,

    @SerializedName("created_by")
    @Expose
    val createdBy: Any? = null,

    @SerializedName("mold_id")
    @Expose
    val moldId: Any? = null,

    @SerializedName("min_value")
    @Expose
    val minValue: Int? = null,

    @SerializedName("seq_data_part")
    @Expose
    val seqDataPart: Int? = null,

    @SerializedName("station_num")
    @Expose
    val stationNum: String? = null,

    @SerializedName("alc_code")
    @Expose
    val alcCode: Any? = null,

    @SerializedName("updated_by")
    @Expose
    val updatedBy: Any? = null,

    @SerializedName("cycle_time")
    @Expose
    val cycleTime: Int? = null,

    @SerializedName("category")
    @Expose
    val category: String? = null,

    @SerializedName("cust_id")
    @Expose
    val custId: String? = null,

    @SerializedName("supplier_id")
    @Expose
    val supplierId: String? = null,

    @SerializedName("part_name")
    @Expose
    val partName: String? = null,

    @SerializedName("customer")
    @Expose
    val customer: String? = null,

    @SerializedName("max_value")
    @Expose
    val maxValue: Int? = null,

    @SerializedName("status")
    @Expose
    val status: Int? = null
)
