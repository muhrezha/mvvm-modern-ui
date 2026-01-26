package co.id.rezha.mycrud.views.AppbarCollapse.fragments.CollapseThird.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.databinding.FragmentNegaraBinding
import co.id.rezha.mycrud.views.AppbarCollapse.fragments.Match
import co.id.rezha.mycrud.views.AppbarCollapse.fragments.MatchAdapter3

class NegaraFragment : Fragment() {

    private var _binding: FragmentNegaraBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNegaraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val matches = List(20) { index ->
            Match("Team ${index + 1} Negara", "Opponent ${index + 1}", "Fr., ${index + 20}.1. 20:30")
        }

        binding.recyclerViewNegara.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewNegara.adapter = MatchAdapter3(matches)

        // Important: Disable nested scrolling untuk RecyclerView di dalam NestedScrollView
        binding.recyclerViewNegara.isNestedScrollingEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}