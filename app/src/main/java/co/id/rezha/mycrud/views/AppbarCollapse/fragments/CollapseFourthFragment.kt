package co.id.rezha.mycrud.views.AppbarCollapse.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.id.rezha.mycrud.R
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CollapseFourthFragment : Fragment() {

    private lateinit var collapsingToolbar: CollapsingToolbarLayout
    private lateinit var fab: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_collapse_fourth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        initializeViews(view)
//        setupToolbar()
//        setupFloatingActionButton()
//        setupAppBarListener()
    }

  /*  private fun initializeViews(view: View) {
        collapsingToolbar = view.findViewById(R.id.collapsing_toolbar)
        fab = view.findViewById(R.id.fab)
    }

    private fun setupToolbar() {
        // Set title yang panjang
        val longTitle = "Ini adalah judul yang sangat panjang yang akan memakan beberapa baris ketika toolbar dalam keadaan expanded dan harus ditampilkan semua tanpa ellipse"

        collapsingToolbar.title = longTitle

        // Force apply styles
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar)
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)
    }

    private fun setupAppBarListener() {
        val appBarLayout = view?.findViewById<com.google.android.material.appbar.AppBarLayout>(R.id.app_bar)

        appBarLayout?.addOnOffsetChangedListener(com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val percentage = Math.abs(verticalOffset).toFloat() / totalScrollRange.toFloat()

            // Optional: Anda bisa menambahkan logika berdasarkan persentase scroll
            // jika diperlukan untuk animasi atau perubahan UI lainnya
        })
    }

    private fun setupFloatingActionButton() {
        fab.setOnClickListener {
            showSnackbar("FAB Clicked!")
        }
    }*/

    private fun showSnackbar(message: String) {
        view?.let {
            com.google.android.material.snackbar.Snackbar.make(
                it,
                message,
                com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        fun newInstance(): CollapseFourthFragment {
            return CollapseFourthFragment()
        }
    }
}