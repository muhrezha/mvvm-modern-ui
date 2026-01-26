package co.id.rezha.mycrud.views.payungin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.views.payungin.views.dashboard.fragments.EventsItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions


class LearningAdapter(private val items: List<EventsItem>) :
    RecyclerView.Adapter<LearningAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivProductEvent)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitleEvent)
        val tvSubtitle: TextView = itemView.findViewById(R.id.tvDateEvent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_events, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // Set data ke views
        holder.tvTitle.text = item.title
        holder.tvSubtitle.text = item.subtitle

        // Load image dengan Glide
        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(32)))
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = items.size
}