package co.id.rezha.mycrud.views.tabbar_case.fragments_tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.databinding.FragmentProfileTabBinding

class ProfileFragmentTab : Fragment() {

    private var _binding: FragmentProfileTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Listen ke result dari Dashboard
        parentFragmentManager.setFragmentResultListener(
            DashboardFragmentTab.RESULT_KEY_FULLNAME,
            this
        ) { _, bundle ->
            val fullname = bundle.getString(DashboardFragmentTab.RESULT_FIELD_FULLNAME).orEmpty()
            // Kalau view belum siap, simpan sementara di arguments atau ViewModel.
            if (_binding != null) {
                binding.tvProfileName.text = "Fullname: $fullname"
            } else {
                // Simpan untuk nanti (opsional)
                arguments = (arguments ?: Bundle()).apply {
                    putString("cached_fullname", fullname)
                }
            }
        }
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentProfileTabBinding.inflate(i, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Tampilkan cache bila ada (misal listener kepanggil duluan)
        val cached = arguments?.getString("cached_fullname")
        if (!cached.isNullOrEmpty()) {
            binding.tvProfileName.text = "Fullname: $cached"
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}