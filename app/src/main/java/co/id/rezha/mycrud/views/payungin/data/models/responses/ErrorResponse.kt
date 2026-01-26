package co.id.rezha.mycrud.views.payungin.data.models.responses

data class ErrorResponse(
    val status: Int? = null,
    val code: String? = null,
    val data: Any? = null,
    val message: String? = null
)