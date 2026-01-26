package co.id.rezha.mycrud.views.tabbar_case

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.databinding.ActivityMainTabBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainTabActivity : AppCompatActivity(), CartActionListener {

    private lateinit var binding: ActivityMainTabBinding
    private var cartCount = 0

    private val userMenu = UserMenu(food = "Food", drink = "Drink")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainTabBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartCount = savedInstanceState?.getInt("cartCount") ?: 0

        val adapter = TabsPagerAdapter(this, userMenu)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> { tab.text = "Dashboard"; tab.setIcon(R.drawable.ic_email) }
                1 -> { tab.text = "Cart";      tab.setIcon(R.drawable.ic_launcher_background) }
                2 -> { tab.text = "Profile";   tab.setIcon(R.drawable.ic_email) }
            }
        }.attach()

        // Tampilkan badge awal (jika count > 0)
        updateCartBadge()

        // (Opsional) reset badge saat tab cart dikunjungi:
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == 1) {
                    // Reset hitungan & sembunyikan badge ketika user membuka tab Cart
                    cartCount = 0
                    updateCartBadge()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("cartCount", cartCount)
        super.onSaveInstanceState(outState)
    }

    // ==== Interface callback dari CartFragment ====
    override fun onAddToCartClicked() {
        cartCount++
        updateCartBadge()
    }

    private var useDotBadge = true // set true untuk dot, false untuk angka

    // Pakai angkat
    /*private fun updateCartBadge() {
        val tab = binding.tabLayout.getTabAt(1) ?: return // index 1 = Cart
        val badge = tab.orCreateBadge
        if (cartCount > 0) {
            badge.isVisible = true
            badge.number = cartCount
            // (Opsional) badge.badgeTextColor / badge.backgroundColor dg ContextCompat.getColor(...)
        } else {
            badge.isVisible = false
            badge.clearNumber()
        }
    }*/

    // Pakai dot/titik
    private fun updateCartBadge() {
        val tab = binding.tabLayout.getTabAt(1) ?: return // index 1 = Cart
        val badge = tab.orCreateBadge

        if (useDotBadge) {
            if (cartCount > 0) {
                badge.isVisible = true
                // Pastikan tidak ada angka:
                badge.clearNumber()     // hilangkan angka jika sebelumnya pernah ada
                // (opsional) warna:
                // badge.backgroundColor = ContextCompat.getColor(this, R.color.teal_700)
            } else {
                badge.isVisible = false
            }
        } else {
            // Mode angka seperti sebelumnya
            if (cartCount > 0) {
                badge.isVisible = true
                badge.number = cartCount
                // (opsional) batasi karakter, contoh "99+"
                badge.maxCharacterCount = 3
            } else {
                badge.isVisible = false
                badge.clearNumber()
            }
        }
    }


    // Dipanggil dari DashboardFragmentTab setelah klik Send untuk pindah ke Profile
    fun navigateToProfile() {
        binding.viewPager.currentItem = 2 // index 2 = Profile
    }

}
