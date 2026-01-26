package co.id.rezha.mycrud.views.tabbar_case.fragments_tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import co.id.rezha.mycrud.databinding.FragmentDashboardTabBinding
import co.id.rezha.mycrud.views.tabbar_case.MainTabActivity
import co.id.rezha.mycrud.views.tabbar_case.UserMenu

class DashboardFragmentTab : Fragment() {

    companion object {
        private const val ARG_USER_MENU = "arg_user_menu"
        const val RESULT_KEY_FULLNAME = "RESULT_FULLNAME"
        const val RESULT_FIELD_FULLNAME = "fullname"

        fun newInstance(userMenu: UserMenu) = DashboardFragmentTab().apply {
            arguments = Bundle().apply { putParcelable(ARG_USER_MENU, userMenu) }
        }
    }
    private var _binding: FragmentDashboardTabBinding? = null
    private val binding get() = _binding!!
    private var userMenu: UserMenu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userMenu = arguments?.let {
            BundleCompat.getParcelable(it, ARG_USER_MENU, UserMenu::class.java)
        }
//        userMenu = arguments?.getParcelable(ARG_USER_MENU)
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentDashboardTabBinding.inflate(i, c, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvFood.text = "Food: ${userMenu?.food ?: "-"}"
        binding.tvDrink.text = "Drink: ${userMenu?.drink ?: "-"}"

        binding.btnSend.setOnClickListener {
            val fullname = binding.etFullname.text?.toString()?.trim().orEmpty()
            if (fullname.isEmpty()) {
                Toast.makeText(requireContext(), "Fullname wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Kirim ke ProfileFragmentTab via Fragment Result API
            parentFragmentManager.setFragmentResult(
                RESULT_KEY_FULLNAME,
                bundleOf(RESULT_FIELD_FULLNAME to fullname)
            )

            // Minta Activity pindah ke tab Profile
            (activity as? MainTabActivity)?.navigateToProfile()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
