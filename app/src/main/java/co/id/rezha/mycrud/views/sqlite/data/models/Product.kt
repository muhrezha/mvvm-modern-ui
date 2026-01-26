package co.id.rezha.mycrud.views.sqlite.data.models

data class Product(
    val id: Long = 0,
    val name: String,
    val stock: Int,
    val price: Double,
    val imagePath: String
)
