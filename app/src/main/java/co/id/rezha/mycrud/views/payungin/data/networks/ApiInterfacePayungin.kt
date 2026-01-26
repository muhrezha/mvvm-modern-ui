package co.id.rezha.mycrud.views.payungin.data.networks

import co.id.rezha.mycrud.views.payungin.data.models.ApiEnvelope
import co.id.rezha.mycrud.views.payungin.data.models.requests.AuthRequest
import co.id.rezha.mycrud.views.payungin.data.models.requests.VerifyEmailRequest
import co.id.rezha.mycrud.views.payungin.data.models.responses.AuthResponse
import co.id.rezha.mycrud.views.payungin.data.models.responses.CategoriesEventResponse
import co.id.rezha.mycrud.views.payungin.data.models.responses.VerifyEmailResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterfacePayungin {

    // LOGIN / VERIFY EMAIL
    @POST("v2/m/public/users/verify-email")
    suspend fun verifyEmail(
        @Body body: VerifyEmailRequest
    ): ApiEnvelope<VerifyEmailResponse>

    @POST("v2/m/public/users/auth")
    suspend fun auth(
        @Body body: AuthRequest
    ): ApiEnvelope<AuthResponse>


    @GET("v3/academy/m/training_categories")
    suspend fun getCategoriesEvent(): ApiEnvelope<List<CategoriesEventResponse>>

//      @POST("$_defaultPath/auth")
//  Future<BaseResponse<TokenModel>> getToken(@Body() SignInRequest signInRequest);

//    @GET("/v2/m/notification")
//    suspend fun getNotifications(): ApiEnvelope<List<NotificationItem>>

//    @GET("/v2/m/profile")
//    suspend fun getProfile(): ApiEnvelope<ProfileResponse>

}