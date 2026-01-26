package co.id.rezha.myroomdb.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import co.id.rezha.core.data.tables.TableUser
import co.id.rezha.myroomdb.R
import co.id.rezha.myroomdb.interfaces.UserCallback

class UserAdapter(
    private val context: Context,
    private var items: MutableList<TableUser>,
    private val callback: UserCallback
) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): TableUser = items[position]

    override fun getItemId(position: Int): Long = items[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.room_item_user, parent, false)

        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvAge = view.findViewById<TextView>(R.id.tvAge)
        val tvGender = view.findViewById<TextView>(R.id.tvGender)
        val btnEdit = view.findViewById<Button>(R.id.btnEdit)

        val item = items[position]

        tvName.text = "Name: ${item.name}"
        tvAge.text = "Age: ${item.age}"
        tvGender.text = "Gender: ${item.gender}"

        btnEdit.setOnClickListener {
            callback.onEditClicked(item)
        }

        return view
    }

    fun updateList(newList: List<TableUser>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}
