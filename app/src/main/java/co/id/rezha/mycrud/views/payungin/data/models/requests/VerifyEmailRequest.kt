package co.id.rezha.mycrud.views.payungin.data.models.requests

import com.google.gson.annotations.SerializedName

data class VerifyEmailRequest(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("email") val email: String,
    @SerializedName("status") val status: String? = null,
    @SerializedName("auth_external_id") val authExternalId: Int? = null,
    @SerializedName("auth_external_type") val authExternalType: String? = "email",
    @SerializedName("is_verify_signup") val isVerifySignup: Boolean? = null,
    @SerializedName("phone_number") val phoneNumber: String? = null,
    @SerializedName("activation_code") val activationCode: String? = null,
)
