package co.id.rezha.mycrud.views.payungin.data.models.responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VerifyEmailResponse(
    @SerializedName("id") val id: Int?,
    @SerializedName("email") val email: String?,
    @SerializedName("auth_external_type") val authExternalType: String?,
    @SerializedName("status") val status: String?
) : Parcelable
