package co.id.rezha.mycrud.views.payungin.data.repositories

import android.content.Context
import co.id.rezha.mycrud.views.payungin.data.models.requests.AuthRequest
import co.id.rezha.mycrud.views.payungin.data.models.requests.VerifyEmailRequest
import co.id.rezha.mycrud.views.payungin.data.models.responses.AuthResponse
import co.id.rezha.mycrud.views.payungin.data.models.responses.CategoriesEventResponse
import co.id.rezha.mycrud.views.payungin.data.models.responses.VerifyEmailResponse
import co.id.rezha.mycrud.views.payungin.data.networks.ApiInterfacePayungin
import co.id.rezha.mycrud.views.payungin.data.networks.RetrofitPayungin

class PayunginRepository(context: Context){

    private val api by lazy {
        RetrofitPayungin.getRetrofit(context).create(ApiInterfacePayungin::class.java)
    }

    suspend fun verifyEmail(model: VerifyEmailRequest): VerifyEmailResponse {
        val res = api.verifyEmail(model)
        if (res.data != null) {
            return res.data
        } else {
            throw Exception(res.message ?: "Unknown error")
        }
    }

    suspend fun auth(model: AuthRequest): AuthResponse {
        val res = api.auth(model)
        if (res.data != null) {
            return res.data
        } else {
            throw Exception(res.message ?: "Unknown error")
        }
    }

    suspend fun categoriesEvent(): List<CategoriesEventResponse> {
        val res = api.getCategoriesEvent()
        if (res.data != null) {
            return res.data
        } else {
            throw Exception(res.message ?: "Unknown error")
        }
    }

}