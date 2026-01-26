package co.id.rezha.mycrud.views.grid_case

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.id.rezha.mycrud.R

class GridActivity : AppCompatActivity() , OnCellClickListener {

    private lateinit var rv: RecyclerView
    private lateinit var etCols: EditText
    private lateinit var etRows: EditText
    private lateinit var btnApply: Button

    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var adapter: CellAdapterGrid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)

        rv = findViewById(R.id.rvGrid)
        etCols = findViewById(R.id.etCols)
        etRows = findViewById(R.id.etRows)
        btnApply = findViewById(R.id.btnApply)

        gridLayoutManager = GridLayoutManager(this, 3) // default 3 kolom
        rv.layoutManager = gridLayoutManager
        rv.setHasFixedSize(true)

        adapter = CellAdapterGrid(mutableListOf(), listener = this) {
            gridLayoutManager.spanCount
        }
        rv.adapter = adapter

        btnApply.setOnClickListener { applyGrid() }

        // initial
        etCols.setText("3")
        etRows.setText("2")
        applyGrid()
    }

    private fun applyGrid() {
        val cols = etCols.text.toString().toIntOrNull()?.coerceAtLeast(1) ?: 3
        val rows = etRows.text.toString().toIntOrNull()?.coerceAtLeast(1) ?: 2

        gridLayoutManager.spanCount = cols

        val total = rows * cols
        val data = (1..total).map { CellItem(label = it.toString()) }
        adapter.submit(data)
    }

    override fun onCellClick(position: Int, item: CellItem) {
        Toast.makeText(this, "Klik #$position (${item.label})", Toast.LENGTH_SHORT).show()

        // contoh: ubah label saat diklik
        val cols = gridLayoutManager.spanCount
        val total = (adapter.itemCount).coerceAtLeast(1)
        val newData = (0 until total).map { i ->
            if (i == position) item.copy(label = "✓") else CellItem(label = (i + 1).toString())
        }
        adapter.submit(newData)
    }
}