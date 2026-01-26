package co.id.rezha.mycrud.views.sqlite.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.id.rezha.mycrud.views.sqlite.data.models.Product
import co.id.rezha.mycrud.views.sqlite.data.repositories.ProductRepository
import co.id.rezha.mycrud.views.sqlite.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _productsState = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val productsState: StateFlow<UiState<List<Product>>> = _productsState.asStateFlow()

    private val _updateProductState = MutableStateFlow<UiState<Boolean>>(UiState.Idle)
    val updateProductState: StateFlow<UiState<Boolean>> = _updateProductState.asStateFlow()

    private val _addProductState = MutableStateFlow<UiState<Long>>(UiState.Idle)
    val addProductState: StateFlow<UiState<Long>> = _addProductState.asStateFlow()

    private var currentProducts = mutableListOf<Product>()

    fun getAllProducts() {
        viewModelScope.launch {
            _productsState.value = UiState.Loading
            try {
                val products = repository.getAllProducts()
                currentProducts.clear()
                currentProducts.addAll(products)
                _productsState.value = UiState.Success(products)
            } catch (e: Exception) {
                _productsState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            _addProductState.value = UiState.Loading
            try {
                val newId = repository.insertProduct(product)
                if (newId != -1L) {
                    val newProduct = product.copy(id = newId)
                    currentProducts.add(0, newProduct)
                    _addProductState.value = UiState.Success(newId)
                    // Refresh products list
                    _productsState.value = UiState.Success(currentProducts.toList())
                } else {
                    _addProductState.value = UiState.Error("Failed to add product")
                }
            } catch (e: Exception) {
                _addProductState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateProduct(product: Product, index: Int) {
        viewModelScope.launch {
            _updateProductState.value = UiState.Loading
            try {
                val result = repository.updateProduct(product)
                if (result > 0) {
                    // Update local list without re-querying
                    if (index in currentProducts.indices) {
                        currentProducts[index] = product
                        _productsState.value = UiState.Success(currentProducts.toList())
                    }
                    _updateProductState.value = UiState.Success(true)
                } else {
                    _updateProductState.value = UiState.Error("Failed to update product")
                }
            } catch (e: Exception) {
                _updateProductState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun deleteProduct(id: Long) {
        viewModelScope.launch {
            try {
                repository.deleteProduct(id)
                currentProducts.removeAll { it.id == id }
                _productsState.value = UiState.Success(currentProducts.toList())
            } catch (e: Exception) {
                _productsState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetAddProductState() {
        _addProductState.value = UiState.Idle
    }

    fun resetUpdateProductState() {
        _updateProductState.value = UiState.Idle
    }
}