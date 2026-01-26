package co.id.rezha.mycrud.views.payungin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import co.id.rezha.mycrud.R
import com.bumptech.glide.Glide

class ImageSliderAdapter : RecyclerView.Adapter<ImageSliderAdapter.ViewHolder>() {

    private val imageUrls = mutableListOf<String>()

    fun setImageUrls(urls: List<String>) {
        imageUrls.clear()
        imageUrls.addAll(urls)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_slider_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imageUrls[position])
    }

    override fun getItemCount(): Int = imageUrls.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.sliderImageView)

        fun bind(imageUrl: String) {
            Glide.with(itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_reload_gray) // Placeholder saat loading
                .error(R.drawable.ic_close_red) // Gambar jika error
                .centerCrop()
                .into(imageView)
        }
    }
}