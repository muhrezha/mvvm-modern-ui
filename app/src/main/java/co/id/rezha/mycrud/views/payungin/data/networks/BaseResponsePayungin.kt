package co.id.rezha.mycrud.views.payungin.data.networks

sealed class BaseResponsePayungin<out T> {
    data object Idle : BaseResponsePayungin<Nothing>()
    data object Loading : BaseResponsePayungin<Nothing>()
    data class Success<T>(val data: T) : BaseResponsePayungin<T>()
    data class Error(val code: Int? = null, val message: String? = null, val throwable: Throwable? = null) : BaseResponsePayungin<Nothing>()
}