package co.id.rezha.mycrud.views.fragment_case.fragments


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

// CartFragment.kt
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import co.id.rezha.mycrud.databinding.FragmentCartBinding
import co.id.rezha.mycrud.views.fragment_case.CartBadgeInterface

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private var controller: CartBadgeInterface? = null
    private var localCount = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CartBadgeInterface) controller = context
        else throw IllegalStateException("Parent activity must implement CartBadgeController")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvCount.text = "Items in cart (local): $localCount"

        binding.btnAdd.setOnClickListener {
            localCount++
            binding.tvCount.text = "Items in cart (local): $localCount"
            controller?.onCartAddItem(1)    // update badge di BottomNav (via Activity)
        }

        binding.btnClear.setOnClickListener {
            localCount = 0
            binding.tvCount.text = "Items in cart (local): $localCount"
            controller?.onCartClear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        controller = null
    }
}