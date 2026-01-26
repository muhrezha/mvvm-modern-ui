package co.id.rezha.mycrud.views.AppbarCollapse.fragments.CollapseThird

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.databinding.FragmentCollapseThirdBinding
import co.id.rezha.mycrud.views.AppbarCollapse.fragments.CollapseThird.fragments.ClubFragment
import co.id.rezha.mycrud.views.AppbarCollapse.fragments.CollapseThird.fragments.NegaraFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CollapseThirdFragment : Fragment() {

    private var _binding: FragmentCollapseThirdBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollapseThirdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTabs()
        setupClickListeners()

        // Load initial fragment
        showNegaraFragment()
    }

    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> showNegaraFragment()
                    1 -> showClubFragment()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun showNegaraFragment() {
        childFragmentManager.commit {
            replace(R.id.tabContentContainer, NegaraFragment())
            setReorderingAllowed(true)
        }
    }

    private fun showClubFragment() {
        childFragmentManager.commit {
            replace(R.id.tabContentContainer, ClubFragment())
            setReorderingAllowed(true)
        }
    }

    private fun setupClickListeners() {
        binding.btnWatch.setOnClickListener {
            // Handle watch button click
        }

        binding.btnWatchSmall.setOnClickListener {
            // Handle small watch button click
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}