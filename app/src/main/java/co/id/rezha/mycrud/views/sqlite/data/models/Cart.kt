package co.id.rezha.mycrud.views.sqlite.data.models

data class Cart(
    val id: Long = 0,
    val productId: Long,
    val quantity: Int,
    val dateAdded: String
)
