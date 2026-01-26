package co.id.rezha.mycrud.views.sqlite.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.views.sqlite.ui.adapters.ProductAdapter
import co.id.rezha.mycrud.views.sqlite.data.models.Product
import co.id.rezha.mycrud.views.sqlite.data.repositories.ProductRepository
import co.id.rezha.mycrud.views.sqlite.state.UiState
import co.id.rezha.mycrud.views.sqlite.ui.adapters.ProductAdapterListener
import co.id.rezha.mycrud.views.sqlite.ui.viewmodels.ProductViewModel
import co.id.rezha.mycrud.views.sqlite.ui.viewmodels.ProductViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/*class ProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}*/

class ProductActivity : AppCompatActivity(), ProductAdapterListener {

    private val viewModel: ProductViewModel by viewModels {
        ProductViewModelFactory(ProductRepository(this))
    }
    private lateinit var adapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        initViews()
        setupRecyclerView()
        observeData()
        viewModel.getAllProducts()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        val fabAdd: FloatingActionButton = findViewById(R.id.fabAddProduct)

        fabAdd.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        /*adapter = ProductAdapter(
            emptyList()) { product, position ->
            showUpdateDialog(product, position)
        }*/
        adapter = ProductAdapter(emptyList(), this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        /*adapter = ProductAdapter(
            products = emptyList(),
            onItemClick = { product, position ->
                showUpdateDialog(product, position)
            },
//            onItemLongPress = { product, position ->
//                showDeleteConfirmation(product, position)
//            }
        )*/
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.productsState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        // Show loading indicator
                    }
                    is UiState.Success -> {
                        adapter.updateProducts(state.data)
                    }
                    is UiState.Error -> {
                        // Show error message
                    }
                    UiState.Idle -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewModel.updateProductState.collectLatest { state ->
                when (state) {
                    is UiState.Success -> {
                        // Successfully updated
                        viewModel.resetUpdateProductState()
                    }
                    is UiState.Error -> {
                        // Show error message
                        viewModel.resetUpdateProductState()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showUpdateDialog(product: Product, position: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_update_product, null)
        val etProductName = dialogView.findViewById<EditText>(R.id.editProductName)

        etProductName.setText(product.name)

        AlertDialog.Builder(this)
            .setTitle("Update Produk")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val newName = etProductName.text.toString().trim()
                if (newName.isNotEmpty()) {
                    val updatedProduct = product.copy(name = newName)
                    viewModel.updateProduct(updatedProduct, position)
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showDeleteConfirmation(product: Product, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Produk")
            .setMessage("Yakin ingin menghapus ${product.name}?")
            .setPositiveButton("Hapus") { _, _ ->
                viewModel.deleteProduct(product.id)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllProducts()
    }


    // ⬅️ Implement interface methods
    override fun onItemClick(product: Product, position: Int) {
        showUpdateDialog(product, position)
    }

    override fun onItemLongPress(product: Product, position: Int) {
        showDeleteConfirmation(product, position)
    }
}