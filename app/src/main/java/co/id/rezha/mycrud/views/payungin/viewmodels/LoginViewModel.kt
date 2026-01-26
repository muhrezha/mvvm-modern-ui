package co.id.rezha.mycrud.views.payungin.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import co.id.rezha.mycrud.views.payungin.data.models.requests.AuthRequest
import co.id.rezha.mycrud.views.payungin.data.models.requests.VerifyEmailRequest
import co.id.rezha.mycrud.views.payungin.data.models.responses.AuthResponse
import co.id.rezha.mycrud.views.payungin.data.models.responses.VerifyEmailResponse
import co.id.rezha.mycrud.views.payungin.data.networks.BaseResponsePayungin
import co.id.rezha.mycrud.views.payungin.data.repositories.PayunginRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = PayunginRepository(application)

    private val _verifyEmailState = MutableStateFlow<BaseResponsePayungin<VerifyEmailResponse>>(BaseResponsePayungin.Idle)
    val verifyEmailState: StateFlow<BaseResponsePayungin<VerifyEmailResponse>> = _verifyEmailState

    private val _authState = MutableStateFlow<BaseResponsePayungin<AuthResponse>>(BaseResponsePayungin.Idle)
    val authState: StateFlow<BaseResponsePayungin<AuthResponse>> = _authState

    fun login(model: VerifyEmailRequest) {
        _verifyEmailState.value = BaseResponsePayungin.Loading
        viewModelScope.launch {
            try {
                val data = repo.verifyEmail(model)
                _verifyEmailState.value = BaseResponsePayungin.Success(data)
            } catch (e: Exception) {
                _verifyEmailState.value = BaseResponsePayungin.Error(message = e.message ?: "Unknown error", throwable = e)
            }
        }
    }

    fun authLogin(model: AuthRequest) {
        _authState.value = BaseResponsePayungin.Loading
        viewModelScope.launch {
            try {
                val data = repo.auth(model)
                _authState.value = BaseResponsePayungin.Success(data)
            } catch (e: Exception) {
                _authState.value = BaseResponsePayungin.Error(message = e.message ?: "Unknown error", throwable = e)
            }
        }
    }
}