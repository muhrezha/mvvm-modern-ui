package co.id.rezha.mycrud.views.sqlite.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.views.sqlite.data.models.Product
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.Locale


interface ProductAdapterListener {
    fun onItemClick(product: Product, position: Int)
    fun onItemLongPress(product: Product, position: Int)
}

class ProductAdapter(
    private var products: List<Product>,
    private val listener: ProductAdapterListener  // ⬅️ Gunakan interface
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvStock: TextView = itemView.findViewById(R.id.tvStock)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val ivProduct: ImageView = itemView.findViewById(R.id.ivProduct)

        fun bind(product: Product) {
            tvName.text = product.name
            tvStock.text = "Stok: ${product.stock}"
            tvPrice.text = formatCurrency(product.price)

            // Load image
            product.imagePath?.let { path ->
                Glide.with(itemView.context)
                    .load(path)
                    .placeholder(R.drawable.ic_product_placeholder)
                    .error(R.drawable.ic_product_placeholder)
                    .into(ivProduct)
            } ?: run {
                ivProduct.setImageResource(R.drawable.ic_product_placeholder)
            }

            // Click listener
            itemView.setOnClickListener {
                listener.onItemClick(product, bindingAdapterPosition)  // ⬅️ Panggil interface method
            }

            // Long press listener
            itemView.setOnLongClickListener {
                listener.onItemLongPress(product, bindingAdapterPosition)  // ⬅️ Panggil interface method
                true
            }
        }

        private fun formatCurrency(amount: Double): String {
            val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            return format.format(amount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }

    fun updateProductAtPosition(product: Product, position: Int) {
        if (position in 0 until products.size) {
            val newList = products.toMutableList()
            newList[position] = product
            products = newList
            notifyItemChanged(position)
        }
    }
}