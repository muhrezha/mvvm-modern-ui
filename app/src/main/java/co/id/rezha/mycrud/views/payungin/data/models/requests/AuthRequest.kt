package co.id.rezha.mycrud.views.payungin.data.models.requests

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    @SerializedName("email")
    val email: String? = null,

    @SerializedName("password")
    val password: String? = null,

    @SerializedName("scope")
    val scope: String? = null,

    @SerializedName("grant_type")
    val grantType: String? = null,

    @SerializedName("auth_external_id")
    val authExternalId: String? = null,

    @SerializedName("fcm_token")
    val fcmToken: String? = null
)