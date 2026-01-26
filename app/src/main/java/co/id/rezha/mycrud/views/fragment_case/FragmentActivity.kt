package co.id.rezha.mycrud.views.fragment_case

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import co.id.rezha.core.helpers.Consts
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.views.fragment_case.fragments.CartFragment
import co.id.rezha.mycrud.views.fragment_case.fragments.DashboardFragment
import co.id.rezha.mycrud.views.fragment_case.fragments.ProfileFragment
import co.id.rezha.mycrud.views.fragment_case.fragments.UserModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class FragmentActivity : AppCompatActivity(), CartBadgeInterface {


    private lateinit var bottomNav: BottomNavigationView
    private var cartCount: Int = 0

    // contoh data user dari BE/Prefs
    private val userModel = UserModel(nama = "Rezha", usia = 30)

    companion object {
        private const val KEY_CART_COUNT = "key_cart_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        try {
            bottomNav = findViewById(R.id.bottom_nav)

            // restore badge count if exists
            cartCount = savedInstanceState?.getInt(KEY_CART_COUNT) ?: 0
            applyCartBadge()

            // default fragment
            if (savedInstanceState == null) {
                replace(DashboardFragment())
            }

            bottomNav.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_dashboard -> replace(DashboardFragment())
                    R.id.nav_cart -> replace(CartFragment())
                    R.id.nav_learning -> replace(ProfileFragment.newInstance(userModel))
                }
                true
            }
        }catch (e: Exception){
            Log.e(Consts.TAG,"${e.message}", e);
        }
    }

    private fun replace(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_container, fragment)
            .commit()
    }

    /** CartBadgeInterface impl */
    override fun onCartAddItem(increment: Int) {
        cartCount += increment
        applyCartBadge()
    }

    override fun onCartClear() {
        cartCount = 0
        applyCartBadge()
    }

    private fun applyCartBadge() {
        val badge = bottomNav.getOrCreateBadge(R.id.nav_cart)
        if (cartCount > 0) {
            badge.isVisible = true
            badge.number = cartCount
        } else {
            // sembunyikan jika 0
            badge.isVisible = false
            badge.clearNumber()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CART_COUNT, cartCount)
    }
}