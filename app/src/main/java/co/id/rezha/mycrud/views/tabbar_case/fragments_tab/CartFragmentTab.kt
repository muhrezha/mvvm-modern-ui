package co.id.rezha.mycrud.views.tabbar_case.fragments_tab

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.databinding.FragmentCartTabBinding
import co.id.rezha.mycrud.views.tabbar_case.CartActionListener
import com.google.android.material.snackbar.Snackbar

class CartFragmentTab : Fragment() {

    private var _binding: FragmentCartTabBinding? = null
    private val binding get() = _binding!!
    private var callback: CartActionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Pastikan activity mengimplementasikan interface
        callback = context as? CartActionListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabAdd.setOnClickListener {
            // Beritahu Activity untuk update badge/cartCount
            callback?.onAddToCartClicked()
            // (Opsional) tampilkan feedback
            Snackbar.make(view, "Item ditambahkan", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }
}
