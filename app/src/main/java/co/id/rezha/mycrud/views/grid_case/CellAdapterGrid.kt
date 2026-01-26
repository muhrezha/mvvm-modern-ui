package co.id.rezha.mycrud.views.grid_case

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.id.rezha.mycrud.R
import kotlin.math.max

class CellAdapterGrid(
    private val items: MutableList<CellItem>,
    private val listener: OnCellClickListener,
    private val spanCountProvider: () -> Int   // untuk hitung ukuran kotak
) : RecyclerView.Adapter<CellAdapterGrid.VH>() {

    inner class VH(val v: View) : RecyclerView.ViewHolder(v) {
        val tv = v.findViewById<TextView>(R.id.tvLabel)
        val root = v.findViewById<View>(R.id.root)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cell_grid, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.tv.text = item.label

        // Paksa square: tinggi = lebar kolom
        holder.itemView.post {
            val span = max(1, spanCountProvider())
            val totalSpacing = (holder.itemView.marginLeft + holder.itemView.marginRight + 12).toPx(holder.itemView) // kira-kira
            val cellWidth = (holder.itemView.measuredWidth - totalSpacing) / 1 // measuredWidth dari parent card
            holder.root.layoutParams.height =
                (holder.itemView.rootView.width / span) // sederhana: lebar layar / kolom
            holder.root.requestLayout()
        }

        holder.itemView.setOnClickListener {
            listener.onCellClick(position, item)
        }
    }

    override fun getItemCount() = items.size

    fun submit(newItems: List<CellItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}

private val View.marginLeft get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin ?: 0
private val View.marginRight get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin ?: 0
private fun Int.toPx(view: View) = (this * view.resources.displayMetrics.density).toInt()
