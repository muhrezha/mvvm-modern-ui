package co.id.rezha.mycrud.views.sqlite.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.views.sqlite.data.models.Product
import co.id.rezha.mycrud.views.sqlite.data.repositories.ProductRepository
import co.id.rezha.mycrud.views.sqlite.state.UiState
import co.id.rezha.mycrud.views.sqlite.ui.viewmodels.ProductViewModel
import co.id.rezha.mycrud.views.sqlite.ui.viewmodels.ProductViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

//class FormActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContentView(R.layout.activity_form)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//    }
//}

class FormActivity : AppCompatActivity() {

    private val viewModel: ProductViewModel by viewModels {
        ProductViewModelFactory(ProductRepository(this))
    }
    private var imagePath: String? = null

    private lateinit var etName: EditText
    private lateinit var etStock: EditText
    private lateinit var etPrice: EditText
    private lateinit var ivProduct: ImageView
    private lateinit var btnSave: Button
    private lateinit var btnPickImage: Button

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                imagePath = uri.toString()
                ivProduct.setImageURI(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        initViews()
        setupClickListeners()
        observeData()
    }

    private fun initViews() {
        etName = findViewById(R.id.etName)
        etStock = findViewById(R.id.etStock)
        etPrice = findViewById(R.id.etPrice)
        ivProduct = findViewById(R.id.ivProduct)
        btnSave = findViewById(R.id.btnSave)
        btnPickImage = findViewById(R.id.btnPickImage)
    }

    private fun setupClickListeners() {
        btnPickImage.setOnClickListener {
            openGallery()
        }

        btnSave.setOnClickListener {
            saveProduct()
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.addProductState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        btnSave.isEnabled = false
                        btnSave.text = "Menyimpan..."
                    }
                    is UiState.Success -> {
                        btnSave.isEnabled = true
                        btnSave.text = "Simpan"
                        Toast.makeText(this@FormActivity, "Produk berhasil disimpan", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is UiState.Error -> {
                        btnSave.isEnabled = true
                        btnSave.text = "Simpan"
                        Toast.makeText(this@FormActivity, "Error: ${state.message}", Toast.LENGTH_SHORT).show()
                    }
                    UiState.Idle -> {
                        btnSave.isEnabled = true
                        btnSave.text = "Simpan"
                    }
                }
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun saveProduct() {
        val name = etName.text.toString().trim()
        val stockText = etStock.text.toString().trim()
        val priceText = etPrice.text.toString().trim()

        if (name.isEmpty() || stockText.isEmpty() || priceText.isEmpty()) {
            Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show()
            return
        }

        val stock = stockText.toIntOrNull()
        val price = priceText.toDoubleOrNull()

        if (stock == null || price == null) {
            Toast.makeText(this, "Stok dan harga harus angka", Toast.LENGTH_SHORT).show()
            return
        }

        val product = Product(
            name = name,
            stock = stock,
            price = price,
            imagePath = imagePath ?: ""
        )

        viewModel.addProduct(product)
    }
}