package co.id.rezha.mycrud.views.payungin.data.models

import com.google.gson.annotations.SerializedName

/**
 * Bentuk respons umum: { status:200, code:200, data:{...}, message:"Success" }
 */
data class ApiEnvelope<T>(
    @SerializedName("status") val status: Int?,
    @SerializedName("code") val code: Int?,
    @SerializedName("data") val data: T?,
    @SerializedName("message") val message: String?
)