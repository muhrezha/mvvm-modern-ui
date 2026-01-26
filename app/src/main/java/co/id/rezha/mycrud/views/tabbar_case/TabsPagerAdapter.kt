package co.id.rezha.mycrud.views.tabbar_case

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import co.id.rezha.mycrud.views.tabbar_case.fragments_tab.CartFragmentTab
import co.id.rezha.mycrud.views.tabbar_case.fragments_tab.DashboardFragmentTab
import co.id.rezha.mycrud.views.tabbar_case.fragments_tab.ProfileFragmentTab

class TabsPagerAdapter(
    activity: MainTabActivity,
    private val userMenu: UserMenu
) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 3
    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> DashboardFragmentTab.newInstance(userMenu)
        1 -> CartFragmentTab()
        else -> ProfileFragmentTab()
    }
}
