package co.id.rezha.mycrud.views.AppbarCollapse.fragments.CollapseThird

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import co.id.rezha.mycrud.views.AppbarCollapse.fragments.CollapseThird.fragments.ClubFragment
import co.id.rezha.mycrud.views.AppbarCollapse.fragments.CollapseThird.fragments.NegaraFragment

class TabPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NegaraFragment()
            1 -> ClubFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}