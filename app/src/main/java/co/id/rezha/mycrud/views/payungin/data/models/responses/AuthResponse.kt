package co.id.rezha.mycrud.views.payungin.data.models.responses

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("refresh_token")
    val refreshToken: String? = null,

    @SerializedName("token_type")
    val tokenType: String? = null,

    @SerializedName("access_token")
    val accessToken: String? = null,

    @SerializedName("expires_in")
    val expiresIn: Int? = null,

    @SerializedName("fcm_token")
    val fcmToken: String? = null,

    @SerializedName("is_deletion_canceled")
    val isDeletionCanceled: Boolean? = null,

    @SerializedName("is_pakar_active")
    val isPakarActive: String? = null,

    @SerializedName("papri_refresh_token")
    val papriRefreshToken: String? = null,

    @SerializedName("pakar_refresh_token")
    val pakarRefreshToken: String? = null,

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("auth_external_id")
    val authExternalId: String? = null,

    @SerializedName("auth_external_type")
    val authExternalType: String? = null,

    @SerializedName("is_verify_signup")
    val isVerifySignup: Boolean? = null,

    @SerializedName("phone_number")
    val phoneNumber: String? = null,

    @SerializedName("activation_code")
    val activationCode: String? = null
) {
    // Optional: Anda bisa menambah function helper jika diperlukan
    fun isTokenValid(): Boolean {
        return !accessToken.isNullOrEmpty()
    }

    fun getFullToken(): String {
        return if (!tokenType.isNullOrEmpty() && !accessToken.isNullOrEmpty()) {
            "$tokenType $accessToken"
        } else {
            accessToken ?: ""
        }
    }
}
