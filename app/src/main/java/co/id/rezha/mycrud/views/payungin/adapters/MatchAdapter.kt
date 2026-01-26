package co.id.rezha.mycrud.views.payungin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.id.rezha.mycrud.databinding.ItemMatchBinding

class MatchAdapter(private val items: List<String>) :
    RecyclerView.Adapter<MatchAdapter.VH>() {

    inner class VH(val b: ItemMatchBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.b.tvDate.text = items[position]
    }

    override fun getItemCount() = items.size
}
