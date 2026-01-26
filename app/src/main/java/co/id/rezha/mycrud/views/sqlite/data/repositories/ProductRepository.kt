package co.id.rezha.mycrud.views.sqlite.data.repositories

import android.content.Context
import co.id.rezha.mycrud.views.sqlite.data.local.DatabaseHelper
import co.id.rezha.mycrud.views.sqlite.data.models.Product

class ProductRepository(private val context: Context) {

    private val databaseHelper by lazy { DatabaseHelper(context) }

    suspend fun getAllProducts(): List<Product> {
        return databaseHelper.getAllProducts()
    }

    suspend fun insertProduct(product: Product): Long {
        return databaseHelper.insertProduct(product)
    }

    suspend fun updateProduct(product: Product): Int {
        return databaseHelper.updateProduct(product)
    }

    suspend fun deleteProduct(id: Long): Int {
        return databaseHelper.deleteProduct(id)
    }

    suspend fun getProductById(id: Long): Product? {
        return databaseHelper.getProductById(id)
    }
}